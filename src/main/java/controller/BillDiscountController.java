package controller;

import beans.Cart;
import services.ProductServices;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "BillDiscountController", value = "/BillDiscountController")
public class BillDiscountController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String discountCode = request.getParameter("coupon_code");
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null)
            cart = Cart.getInstance();
        double rate = ProductServices.getInstance().discountCodeRate(discountCode);
        if (rate == 0) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            session.setAttribute("rate", rate);
            session.setAttribute("discountCode", discountCode);
            cart.setRate(rate);
            session.setAttribute("cart", cart);
        }
    }
}
