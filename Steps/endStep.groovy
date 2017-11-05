endStep = stepFactory.createStep()

endStep.run = {
	println "endStep.run"
  	g.V.each { player->
      player.text = c.get("Finished", player.score)
      
      if (player.sender) {
        eventName = "SenderInfo-" + player.id
        a.addEvent(eventName, 
         ["id":player.id,
          "sender":player.sender,
          "receiver":player.receiver,
          "previousPartners":player.previousPartners,
          "amountSentArray": player.amountSentArray,
          "score": player.score
          ])

      } else if (player.receiver) {
        eventName = "ReceiverInfo-" + player.id
        a.addEvent(eventName, 
         ["id":player.id,
          "sender":player.sender,
          "receiver":player.receiver,
          "previousPartners":player.previousPartners,
          "costArray": player.costArray,
          "acceptedCostArray": player.acceptedCostArray,
          "amountReceivedArray": player.amountReceivedArray,          
          "firstRoundAccept": player.firstRoundAccept,
          "secondRoundAccept": player.secondRoundAccept,
          "thirdRoundAccept": player.thirdRoundAccept,
          "score": player.score
          ])
      }
      
    }
}

endStep.done = {
	println "endStep.done"
}
