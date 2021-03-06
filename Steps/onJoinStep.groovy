onJoinStep = stepFactory.createNoUserActionStep()

onJoinStep.run = { playerId->
  def player = g.getVertex(playerId)
  
  player.active = true // Active players have not been dropped
  
  player.text = c.get("Welcome")
  a.add(player, [name: "Next", result: {
    player.text = c.get("Tutorial1")
  }])
  a.add(player, [name: "Next", result: {
    player.text = c.get("Tutorial2", 0, score)
  }])
  a.add(player, [name: "Next", result: {
    player.text = c.get("Tutorial3")
  }])
  a.add(player, [name: "Next", result: {
    player.text = c.get("PleaseWait")
  }])
}
onJoinStep.done = {
}