/*
 * 
 * Copyright 2012 FORTH-ICS-ISL (http://www.ics.forth.gr/isl/) 
 * Foundation for Research and Technology - Hellas (FORTH)
 * Institute of Computer Science (ICS) 
 * Information Systems Laboratory (ISL)
 * 
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent 
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * 
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 * 
 */
package gr.forth.ics.isl.xsearch.admin;

import gr.forth.ics.isl.xsearch.opensearch.DescriptionDocument;
import gr.forth.ics.isl.xsearch.resources.Resources;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Pavlos Fafalios (fafalios@ics.forth.gr, fafalios@csd.uoc.gr)
 */
public class ChangeDescriptionDocument extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();

        String ses = request.getParameter("ses");
        if (ses == null) {
            ses = "";
        }
        
        if (!ses.equals("y")) {
            String loggedin = (String) session.getAttribute("loggedin");
            if (loggedin == null) {
                loggedin = "no";
                session.setAttribute("loggedin", loggedin);
                RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }



        PrintWriter out = response.getWriter();
        out.print("");

        String doc = request.getParameter("doc");
        if (doc == null) {
            doc = "";
        }
        doc = URLDecoder.decode(doc, "utf-8");

        if (doc.toLowerCase().startsWith("figis") || doc.toLowerCase().startsWith("ecoscope")) {
            if (ses.equals("y")) {
                session.setAttribute("descrDoc", doc);
                System.out.println("# New session-based description document: " + doc);
            } else {
                Resources.DESCRIPTIONDOCUMENT = doc;
                System.out.println("# New description document: " + doc);
            }
            
            out.close();
            return;
        }

        DescriptionDocument descrDoc = null;
        try {
            descrDoc = new DescriptionDocument(doc);
        } catch (Exception e) {
            System.out.println("*** ERROR READING DESCRIPTION DOCUMENT!");
            out.print("Error reading description document!");
            return;
        }

        if (descrDoc.isErrorReadingDocument()) {
            out.print("Error reading description document!");
        } else {

            if (descrDoc.getUrlTemplates().isEmpty()) {
                System.out.println("=> Attention! No URL templates in this document! The description document was not changed!");
                out.print("No URL templates!");
            } else {

                if (!descrDoc.getUrlTemplates().containsKey("application/rss+xml") && !descrDoc.getUrlTemplates().containsKey("application/atom+xml")) {
                    out.print("No supported template types! For the present we support only 'application/rss+xml' and 'application/atom+xml' results.");
                } else {
                    
                    if (ses.equals("y")) {
                        session.setAttribute("descrDoc", doc);
                        System.out.println("# New session-based description document: " + doc);
                    } else {
                        Resources.DESCRIPTIONDOCUMENT = doc;
                        System.out.println("# New description document: " + doc);
                    }
                    System.out.println("# URL templates: " + descrDoc.getUrlTemplates());
                }
            }

        }

        out.close();

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
