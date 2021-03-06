/**
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */

package akka.crdt.convergent

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import play.api.libs.json.Json._

class PNCounterSpec extends WordSpec with MustMatchers {
  val node1 = "node1"
  val node2 = "node2"

  "A PNCounter" must {

    "be able to increment each node's record by one" in {
      val c1 = PNCounter(id = "users")

      val c2 = c1 + node1
      val c3 = c2 + node1

      val c4 = c3 + node2
      val c5 = c4 + node2
      val c6 = c5 + node2

      c6.increments.state(node1) must be(2)
      c6.increments.state(node2) must be(3)
    }

    "be able to decrement each node's record by one" in {
      val c1 = PNCounter(id = "users")

      val c2 = c1 - node1
      val c3 = c2 - node1

      val c4 = c3 - node2
      val c5 = c4 - node2
      val c6 = c5 - node2

      c6.decrements.state(node1) must be(2)
      c6.decrements.state(node2) must be(3)
    }

    "be able to increment each node's record by arbitrary delta" in {
      val c1 = PNCounter(id = "users")

      val c2 = c1 + (node1, 3)
      val c3 = c2 + (node1, 4)

      val c4 = c3 + (node2, 2)
      val c5 = c4 + (node2, 7)
      val c6 = c5 + node2

      c6.increments.state(node1) must be(7)
      c6.increments.state(node2) must be(10)
    }

    "be able to decrement each node's record by arbitrary delta" in {
      val c1 = PNCounter(id = "users")

      val c2 = c1 - (node1, 3)
      val c3 = c2 - (node1, 4)

      val c4 = c3 - (node2, 2)
      val c5 = c4 - (node2, 7)
      val c6 = c5 - node2

      c6.decrements.state(node1) must be(7)
      c6.decrements.state(node2) must be(10)
    }

    "be able to increment and decrement each node's record by arbitrary delta" in {
      val c1 = PNCounter(id = "users")

      val c2 = c1 + (node1, 3)
      val c3 = c2 - (node1, 2)

      val c4 = c3 + (node2, 5)
      val c5 = c4 - (node2, 2)
      val c6 = c5 + node2

      c6.increments.value must be(9)
      c6.decrements.value must be(4)
    }

    "be able to summarize the history to the correct aggregated value of increments and decrements" in {
      val c1 = PNCounter(id = "users")

      val c2 = c1 + (node1, 3)
      val c3 = c2 - (node1, 2)

      val c4 = c3 + (node2, 5)
      val c5 = c4 - (node2, 2)
      val c6 = c5 + node2

      c6.increments.value must be(9)
      c6.decrements.value must be(4)

      c6.value must be(5)
    }

    "be able to have its history correctly merged with another GCounter" in {
      // counter 1
      val c11 = PNCounter(id = "users")
      val c12 = c11 + (node1, 3)
      val c13 = c12 - (node1, 2)
      val c14 = c13 + (node2, 5)
      val c15 = c14 - (node2, 2)
      val c16 = c15 + node2

      c16.increments.value must be(9)
      c16.decrements.value must be(4)
      c16.value must be(5)

      // counter 1
      val c21 = PNCounter(id = "users")
      val c22 = c21 + (node1, 2)
      val c23 = c22 - (node1, 3)
      val c24 = c23 + (node2, 3)
      val c25 = c24 - (node2, 2)
      val c26 = c25 + node2

      c26.increments.value must be(6)
      c26.decrements.value must be(5)
      c26.value must be(1)

      // merge both ways
      val merged1 = c16 merge c26
      merged1.increments.value must be(9)
      merged1.decrements.value must be(5)
      merged1.value must be(4)

      val merged2 = c26 merge c16
      merged2.increments.value must be(9)
      merged2.decrements.value must be(5)
      merged2.value must be(4)
    }

    "be able to serialize itself to JSON" in {
      val c1 = PNCounter(id = "users")

      stringify(c1.toJson) must be("""{"type":"pn-counter","id":"users","increments":{"type":"g-counter","id":"users/inc","state":{}},"decrements":{"type":"g-counter","id":"users/dec","state":{}}}""")
      c1.toString must be("""{"type":"pn-counter","id":"users","increments":{"type":"g-counter","id":"users/inc","state":{}},"decrements":{"type":"g-counter","id":"users/dec","state":{}}}""")

      val c2 = c1 + (node1, 3)
      val c3 = c2 - (node1, 2)

      val c4 = c3 + (node2, 5)
      val c5 = c4 - (node2, 2)
      val c6 = c5 + node2

      c6.toString must be("""{"type":"pn-counter","id":"users","increments":{"type":"g-counter","id":"users/inc","state":{"node1":3,"node2":6}},"decrements":{"type":"g-counter","id":"users/dec","state":{"node1":2,"node2":2}}}""")
    }

    "be able to serialize itself from JSON" in {
      val json = parse("""{"type":"pn-counter","id":"users","increments":{"type":"g-counter","id":"users/inc","state":{"node1":3,"node2":6}},"decrements":{"type":"g-counter","id":"users/dec","state":{"node1":2,"node2":2}}}""")
      val c1 = json.as[PNCounter]

      c1.increments.value must be(9)
      c1.decrements.value must be(4)
    }

    "be able to serialize its view to JSON" in {
      val c1 = PNCounter(id = "users")

      val c2 = c1 + (node1, 3)
      val c3 = c2 - (node1, 2)

      val c4 = c3 + (node2, 5)
      val c5 = c4 - (node2, 2)
      val c6 = c5 + node2

      c6.view.toString must be("""{"type":"counter","id":"users","value":5}""")
    }

    "be able to serialize its view from JSON" in {
      val json = parse("""{"type":"counter","id":"users","value":5}""")
      val c1 = json.as[PNCounterView]

      c1.id must be("users")
      c1.value must be(5)
    }
  }
}
