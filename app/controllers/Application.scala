package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import traits._

object Application extends Controller with Secured {

  val loginForm = Form(
    tuple( 
      "username" -> text,
      "password" -> text 
    ) verifying ( "Invalid username or password", result => result match {
      case (u, p) => User.authenticate(u, p).isDefined })
  ) 

  def transactions = TODO
  
  def home = Action {
    Ok(views.html.decoy.home())
  }
  
  def about = Action {
    Ok(views.html.decoy.about())
  }
  
  def humanRights = Action {
    Ok(views.html.decoy.human_rights())
  }
  
  def cleanWater = Action {
    Ok(views.html.decoy.clean_water())
  }
  
  def mobileAid = Action {
    Ok(views.html.decoy.mobile_aid())
  }
  
  def login = Action { implicit request => 
    Ok(views.html.login(loginForm))
  }

  def logout = Action { 
    Redirect(routes.Application.home).withNewSession
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.Products.products).withSession("username" -> user._1)
    )
  }
  
  def order = IsAuthenticated { implicit user => implicit request => 
	  Ok(views.html.order(user))
  }

  def users = TODO

  def javascriptRoutes = Action { implicit request =>
    import controllers.api.routes.{javascript => capi}
    import controllers.routes.{javascript => c}
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        capi.Products.all,
        capi.Products.create,
        capi.Products.update,
        capi.Products.updateMany,
        capi.Products.delete,
        capi.Products.get,
        capi.Products.getTags,
        capi.Products.addTags,
        capi.Products.removeTags,
        capi.Tags.all, 
        capi.Tags.get,
        capi.Tags.getProducts,
        c.Products.viewProduct,
        c.Products.editProduct,
        c.Products.getIcon
      )
    ).as("text/javascript")
  }
  
}

