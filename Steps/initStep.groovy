// Global variables
curRound = 0
nRounds = 3
curStep = "start"
idleTime = 10000

aiBehavior = { player->
  if (player.getProperty("choices")) {
    def choices = player.getProperty("choices")
    def choice = choices[r.nextInt(choices.size())]
    def params = null
    if (curStep == "senderStep" && player.sender) {
      params = [amountSent: (r.nextInt(score)).toString()]
    } else if (curStep == "receiverStep" && player.receiver) {
      params = [amountReturned: (r.nextInt(score)).toString()] 
    }
    a.choose(choice.uid, params)
  }
}

initStep = stepFactory.createStep()

initStep.run = {
  println "initStep.run"
  curStep = "init"
  
  // Let the players know the game will be starting shortly
  g.V.each { player->
    player.text = c.get("PleaseWait") + "<p><strong>Click 'Begin' to join the game.</strong></p>"
    a.add(player, [name: "Begin", result: {
      player.text = c.get("PleaseWait") + "<p><strong>Thank you, the game will begin in a moment.</strong></p>"
    }])
  }


  // Assign vertices as either a sender or receiver
  def n = g.V.size()
  g.V.shuffle.eachWithIndex { player, i->
    player.previousPartners = []
    player.score = score
    if (i % 2 == 1) {
      player.sender = true
      player.receiver = false
      player.amountSentArray = []
    } else {
      player.sender = false
      player.receiver = true
      player.firstRoundAccept = "NA"
      player.secondRoundAccept = "NA"
      player.thirdRoundAccept = "NA"
      player.amountReceivedArray = []
      player.costArray = []
      player.acceptedCostArray = []
    }
  }
}

initStep.done = {
  println "initStep.done"
  pairingStep.start()
}