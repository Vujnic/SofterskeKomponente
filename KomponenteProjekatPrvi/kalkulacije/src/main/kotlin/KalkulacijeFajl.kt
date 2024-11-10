package kalkulacije

fun prosekRedova(data: Map<String, List<String>>): Map<String, List<String>> {
    val brojUcenika = data["id"]?.size ?: 0  // Broj učenika
    val result = data.toMutableMap()  // Kopiramo postojeću mapu da dodamo novu kolonu "prosek"
    val proseci = mutableListOf<String>()  // Lista u kojoj ćemo čuvati proseke učenika

    // Iteriramo kroz svakog učenika
    for (i in 0 until brojUcenika) {
        var total = 0
        var count = 0

        // Sabiramo ocene za svakog učenika kroz sve predmete
        for (key in data.keys) {
            if (key != "id") {
                val ocena = data[key]?.get(i)?.toIntOrNull()
                if (ocena != null) {
                    total += ocena
                    count++
                }
            }
        }

        // Računamo prosek ako postoje ocene
        val prosek = if (count > 0) total / count.toDouble() else 0.0
        proseci.add(prosek.toString())  // Dodajemo izračunati prosek u listu proseka
    }

    result["prosek"] = proseci  // Dodajemo novu kolonu "prosek" u rezultujuću mapu

    return result
}

fun zbirURedovima(data: Map<String, List<String>>): Map<String, List<String>> {
    val brojUcenika = data["id"]?.size ?: 0
    val result = data.toMutableMap()
    val proseci = mutableListOf<String>()


    for (i in 0 until brojUcenika) {
        var total = 0

        for (key in data.keys) {
            if (key != "id") {
                val ocena = data[key]?.get(i)?.toIntOrNull()
                if (ocena != null) {
                    total += ocena
                }
            }
        }

        val prosek = total
        proseci.add(prosek.toString())
    }

    result["prosek"] = proseci

    return result
}


fun prosek(data: Map<String, List<String>>, s: String): Double {
    var suma = 0.0
    val ocene = data[s]
    var bro = 0
    if (ocene != null) {
        for (i in ocene) {
            suma += i.toDouble()
            bro++
        }
    } else {
        return 0.0
    }
    return suma / bro
}

fun suma(data: Map<String, List<String>>, s: String): Int {
    var suma = 0
    val ocene = data[s]
    if (ocene != null) {
        for (i in ocene) {
            suma += i.toInt()
        }
    } else {
        return 0
    }
    return suma
}

fun brojac(data: Map<String, List<String>>, p: String): Int {
    var br = 0
    for ((_, ocene) in data) {
        for (ocena in ocene) {
            if (ocena == p) {
                br++
            }
        }
    }
    return br
}
