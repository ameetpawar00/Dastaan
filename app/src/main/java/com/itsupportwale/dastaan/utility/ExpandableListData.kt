package com.itsupportwale.dastaan.utility

internal object ExpandableListData {
    val data: HashMap<String, List<String>>
        get() {
            val expandableListDetail =
                    HashMap<String, List<String>>()
            val myFavCricketPlayers: MutableList<String> =
                    ArrayList()
            myFavCricketPlayers.add("MS.Dhoni")
            myFavCricketPlayers.add("Sehwag")
            myFavCricketPlayers.add("Shane Watson")
            myFavCricketPlayers.add("Ricky Ponting")
            myFavCricketPlayers.add("Shahid Afridi")
            val myFavFootballPlayers: MutableList<String> = ArrayList()
            myFavFootballPlayers.add("Cristiano Ronaldo")
            myFavFootballPlayers.add("Lionel Messi")
            myFavFootballPlayers.add("Gareth Bale")
            myFavFootballPlayers.add("Neymar JR")
            myFavFootballPlayers.add("David de Gea")
            val myFavTennisPlayers: MutableList<String> = ArrayList()
            myFavTennisPlayers.add("Roger Federer")
            myFavTennisPlayers.add("Rafael Nadal")
            myFavTennisPlayers.add("Andy Murray")
            myFavTennisPlayers.add("Novak Jokovic")
            myFavTennisPlayers.add("Sania Mirza")
            expandableListDetail["ABOUT CRICKET"] = myFavCricketPlayers
            expandableListDetail["ABOUT FOOTBALL"] = myFavFootballPlayers
            expandableListDetail["ABOUT TENNIS"] = myFavTennisPlayers
            return expandableListDetail
        }
}