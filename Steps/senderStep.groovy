costArray = [5, 20]
staticCost = 10

senderStep = stepFactory.createStep()

senderStep.run = {  
    curRound++    
    println "senderStep.run"
    curStep = "sender"
    g.V.filter{it.active && it.receiver}.each { receiver->
      receiver.text = c.get("WaitForSender")
    }
  
    g.V.filter{it.active && it.sender}.each { sender->
        sender.text = c.get("SenderInstructions")
        sender.score = score
        a.add(sender, 
            [name: "Send Money",
            custom: """ 
            <input type="range" class="param" min="0" max="${score}" value="${score/2}" ng-init="amountSent=${score/2}" ng-model="amountSent" name="amountSent" required>
            <p>If your partner cooperates with you in the next step, <strong>you</strong> will receive {{ amountSent * 2 }} cents</p>
            <p>However, if your partner does not cooperate, <strong>you</strong> will lose {{ amountSent }} cents</p>
            """.toString(),

            result: { params->
                if (params != null) {
                    def receiver = sender.neighbors.next()
                    receiver.score = score
                    sender.amountSent = Integer.parseInt(params.a                   
                    if (curRound==1) {
                        thisCostIndex = r.nextInt(costArray.size())
                        thisCost = costArray[thisCostIndex]
                        thisCostText = thisCost + "cents"
                        receiver.cost                    
                    } else {
                        receiver.cost = staticCost
                    }

                    receiver.costArray += thisCost
                    sender.amountSentArray += sender.amountSent 
                    receiver.amountReceived = sender.amountSent
                    receiver.amountReceivedArray += receiver.amountReceived
                    receiver.text = c.get("MakeResponse", sender.amountSent, receiver.cost, sender.amountSent*2)
                    sender.text = c.get("WaitResponse", sender.amountSent, sender.amountSent*2)

                    a.add(receiver, 
                        [name: "Accept", 
                        result: { 
                            receiver.accept = true // XXX append to array 
                            if (curRound==1) {
                                receiver.firstRoundAccept = true
                            } else if (curRound == 2) {
                                receiver.secondRoundAccept = true
                            } else if (curRound == 3) {
                                receiver.thirdRoundAccept = true
                            }
                            receiver.score -= receiver.cost
                            receiver.score +=sender.amountSent
                            sender.score += sender.amountSent * 2
                            sender.text = c.get("ReceiverCooperatedSenderInstructions",  sender.amountSent*2)
                            receiver.text = c.get("ReceiverCooperatedReceiverInstructions", receiver.cost, sender.amountSent*2)
                            receiver.acceptedCostArray += 1
                        }],
                        [name: "Reject", 
                        result: { 
                            receiver.accept = false
                            if (curRound==1) {
                                receiver.firstRoundAccept = false
                            } else if (curRound==2) {
                                receiver.secondRoundAccept = false
                            } else if (curRound == 3) {
                                receiver.thirdRoundAccept = false
                            }
                            receiver.score += sender.amountSent
                            sender.score -= sender.amountSent
                            sender.text = c.get("ReceiverDefectedSenderInstructions", sender.amountSent)
                            receiver.text = c.get("ReceiverDefectedReceiverInstructions", sender.amountSent)
                            receiver.acceptedCostArray += 0
                        }])
                  }
        }])
    }
}

senderStep.done = {
    println "senderStep.done"
    if (curRound < nRounds) {
        println("curRound = " + curRound)
        pairingStep.start()
    } else {
        endStep.start()
    }
}