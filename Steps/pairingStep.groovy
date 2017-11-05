pairingStep = stepFactory.createStep()

pairingStep.run = {
  println "pairingStep.run"
  curStep = "pairing"
  
  // Remove existing edges
  g.removeEdges()
  
  // Pair senders with receivers they haven't been paired with before
  g.V.filter{ it.sender }.shuffle.each { sender->
    def receivers = g.V.filter{ it.receiver }.shuffle.toList()
    for (def i = 0; i < receivers.size(); i++) {
      def receiver = receivers.get(i)
      if (receiver.neighbors.count() == 0 && !sender.previousPartners.contains(receiver.id)) {
        g.addTrackedEdge(sender, receiver, "connected")
        sender.previousPartners += receiver.id
        receiver.previousPartners += sender.id
        break
      } else {
        println("unpaired sender")
        sender.dropped = true 
      }
    }
  }
  
}

pairingStep.done = {
  println "pairingStep.done"
  senderStep.start()
}