package org.http4s
package rho

import org.specs2.mutable.Specification

import UriTemplate.ParamExp
import UriTemplate.PathElm
import UriTemplate.PathExp

class RouteAsUriTemplateSpec extends Specification {

  "PathBuilder as UriTemplate" should {
    "convert to /hello" in {
      val route = GET / "hello"
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathElm("hello"))))
    }
    "convert to /hello/world" in {
      val route = GET / "hello" / "world"
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathElm("hello"), PathElm("world"))))
    }
    "convert to /hello{/world}" in {
      val route = GET / "hello" / 'world
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathElm("hello"), PathExp("world"))))
    }
    "convert to /hello/world/next/time" in {
      val route1 = "hello" / "world"
      val route2 = "next" / "time"
      val route = GET / route1 / route2
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathElm("hello"), PathElm("world"), PathElm("next"), PathElm("time"))))
    }
    "convert to {/id}" in {
      val route = GET / pathVar[Int]("id")
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathExp("id"))))
    }
    "convert to {/unknown}" in {
      val route = GET / pathVar[Int]
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathExp("unknown"))))
      true
    }
    "convert to /orders{/id}/items" in {
      val route = GET / "orders" / pathVar[Int]("id") / "items"
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathElm("orders"), PathExp("id"), PathElm("items"))))
    }
  }

  "QueryBuilder as UriTemplate" should {
    "convert to /hello{?world}" in {
      val route = GET / "hello" +? query[Int]("world")
      val p = List(PathElm("hello"))
      val q = Some(List(ParamExp("world")))
      route.asUriTemplate must equalTo(UriTemplate(path = p, query = q))
    }
    "convert to /hello/world{?start}{&start}" in {
      val route = GET / "hello" / "world" +? query[Int]("start") & query[Int]("limit")
      val p = List(PathElm("hello"), PathElm("world"))
      val q = Some(List(ParamExp("start"), ParamExp("limit")))
      route.asUriTemplate must equalTo(UriTemplate(path = p, query = q))
    }
  }

  "TypedPath as UriTemplate" should {
    "convert to /hello" in {
      val route = GET / "hello"
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathElm("hello"))))
    }
    "convert to /hello/world" in {
      val route = "hello" / "world"
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathElm("hello"), PathElm("world"))))
    }
    "convert to /hello{/world}" in {
      val route = "hello" / 'world
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathElm("hello"), PathExp("world"))))
    }
    "convert to /hello/world/next/time" in {
      val route1 = "hello" / "world"
      val route2 = "next" / "time"
      val route = route1 && route2
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathElm("hello"), PathElm("world"), PathElm("next"), PathElm("time"))))
    }
    "convert to {/id}" in {
      val route = pathVar[Int]("id")
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathExp("id"))))
    }
    "convert to {/unknown}" in {
      val route = pathVar[Int]
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathExp("unknown"))))
      true
    }
    "convert to /orders{/id}/items" in {
      val route = "orders" / pathVar[Int]("id") / "items"
      route.asUriTemplate must equalTo(UriTemplate(path = List(PathElm("orders"), PathExp("id"), PathElm("items"))))
    }
  }

  "TypedQuery as UriTemplate" should {
    "convert to {?world}" in {
      val route = query[Int]("world")
      val q = Some(List(ParamExp("world")))
      route.asUriTemplate must equalTo(UriTemplate(query = q))
    }
    "convert to {?start}{&start}" in {
      val route = query[Int]("start") && query[Int]("limit")
      val q = Some(List(ParamExp("start"), ParamExp("limit")))
      route.asUriTemplate must equalTo(UriTemplate(query = q))
    }
  }

}
