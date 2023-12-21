tasks.register("hello") {
   doLast {
      println("Hello")
   }
}


tasks.register("world") {
   dependsOn("hello")
   
   doLast {
      println("world")
   }
}
