import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import spec.ReportInterface
import kalkulacije.Kalkulacije
import kalkulacije.*
import java.io.InputStreamReader
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*
import java.sql.Connection

data class Schedule(
    val id : Int,
    val ime : String,
    val prezime : String,
    val matematika : Int,
    val informatika : Int,
    val fizika : Int,
    val istorija : Int,
    val geografija : Int
)

fun prepareData(jsonData: InputStreamReader): Map<String, List<String>> {
    val gson = Gson()
    val scheduleType = object : TypeToken<List<Schedule>>() {}.type
    val schedules: List<Schedule> = gson.fromJson(jsonData, scheduleType)

    // Convert the list into a Map<String, List<String>> where key is column name and value is a list of corresponding column data
    val reportData: Map<String, List<String>> = mapOf(
        "id" to schedules.map { it.id.toString() },
        "ime" to schedules.map { it.ime },
        "prezime" to schedules.map { it.prezime},
        "matematika" to schedules.map { it.matematika.toString() },
        "informatika" to schedules.map { it.informatika.toString() },
        "fizika" to schedules.map { it.fizika.toString() },
        "istorija" to schedules.map { it.istorija.toString() },
        "geografija" to schedules.map { it.geografija.toString() }
    )

    return reportData
}

fun connectToDatabase(): Connection {
    val properties = Properties().apply {
        put("user", "root")
        put("password", "MornarPopaj")
    }
    return try {
        DriverManager.getConnection("jdbc:mysql://localhost:3306/skole", properties)
    } catch (e: SQLException) {
        println("Greška prilikom povezivanja sa bazom: ${e.message}")
        throw RuntimeException("Neuspešno povezivanje sa bazom", e)
    }
}

fun fetchDataFromDatabase(connection: Connection, s: String?): Map<String, List<String>> {
    val resultMap = mutableMapOf<String, MutableList<String>>()

    //val query = "SELECT * FROM ucenici"
    val query = s// SQL upit koji uzima sve podatke iz tabele
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(query)

    // Dobijamo metapodatke iz ResultSet-a (imena kolona)
    val metaData = resultSet.metaData
    val columnCount = metaData.columnCount

    // Inicijalizujemo prazne liste za svaku kolonu u mapu
    for (i in 1..columnCount) {
        resultMap[metaData.getColumnName(i)] = mutableListOf()
    }

    // Iteriramo kroz sve redove i dodajemo podatke u odgovarajuće liste
    while (resultSet.next()) {
        for (i in 1..columnCount) {
            val columnName = metaData.getColumnName(i)
            resultMap[columnName]?.add(resultSet.getString(i))
        }
    }

    return resultMap
}

fun main() {

    val connection = connectToDatabase()
    println("Unesi sql kod(SELECT * FROM ucenici)")
    var s = readln()
    if(s.isEmpty()){
        s = "SELECT * FROM ucenici"
    }
    val data = fetchDataFromDatabase(connection, s)

    val serviceLoader = ServiceLoader.load(ReportInterface::class.java)

    val exporterServices = mutableMapOf<String, ReportInterface> ()

    serviceLoader.forEach{
            service ->
        exporterServices[service.implName] = service
    }

    println(exporterServices.keys)

    //val inputStream = object {}.javaClass.getResourceAsStream("/data2.json")
    //val reader = InputStreamReader(inputStream)
    //val data = prepareData(reader)
    //reader.close()

    println(data)


    println("Izaberite zeljeni format")
    println("Moguci formati:\n" +
            "1. CSV\n" +
            "2. txt\n" +
            "3. PDF\n" +
            "4. Eksel")
    val formattxt = readln()
    val st:String
    val tekst:String
    var rezime  = ""
    println("Ako zelite da dodate prosek po predmetu u rezime, unesite zeljeni predmet")
    var ime = readln()

    while(!ime.isEmpty()){
        val a = prosek(data, ime)
        rezime += "$ime - $a\n"
        println("Ako zelite da dodate novi prosek po predmetu u rezime unesite zeljeni predmet")
        ime = readln()
    }

    println("Ako zelite da dodate broj specificne stvari iz tabele u rezime unesite zeljenu element")
    var p = readln()
    while(!p.isEmpty()){
        val b = brojac(data, p)
        rezime += "$p - $b\n"
        println("Ako zelite da dodate broj specificne stvari iz tabele u rezime unesite zeljeni element")
        p = readln()
    }

    if(formattxt.toInt() == 1){
        tekst = "izlaz9.csv"
        st = "CSV"
        val format = exporterServices[st]
        val res = prosekRedova(data)
        format?.generateReport(res, tekst,  true, null, null)
    } else if(formattxt.toInt() == 2){
        tekst = "izlaz9.txt"
        st = "TXT"
        val format = exporterServices[st]
        println("Unesite zeljeni naslov")
        val naslov = readln()
        val res = prosekRedova(data)
        format?.generateReport(res, tekst,  true, naslov, rezime)
    } else if(formattxt.toInt() == 3){
        tekst = "izlaz9.pdf"
        st = "PDF"
        val format = exporterServices[st]
        println("Unesite zeljeni naslov")
        val naslov = readln()
        val res = prosekRedova(data)
        format?.generateReport(res, tekst,  true, naslov, rezime)
    } else {
        tekst = "izlaz9.xlsx"
        st = "XLS"
        val format = exporterServices[st]
        val res = prosekRedova(data)
        format?.generateReport(res, tekst,  true, null, rezime)

    }



}