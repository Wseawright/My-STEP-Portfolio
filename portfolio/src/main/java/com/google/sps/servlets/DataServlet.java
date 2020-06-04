// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.Gson;
import java.util.*;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

//    public ArrayList<String> comments = new ArrayList<String>(List.of("boom", "bam", "pow"));


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // obtain input from form(the user's comment)
    String userComment = getParameter(request, "userCommentInput", "");
    
    // seperate words into indivdual text
    String[] words = userComment.split("\\s*,\\s*");

    // Respond with the result.
    response.setContentType("text/html;");
    response.getWriter().println(Arrays.toString(words));
  }
  
//   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//     // Get the input from the form.
//     response.setContentType("text/html;");
//     String userComment = getParameter(request, "userCommentInput", "");
//     response.getWriter().println(userComment);
//   }
  
  public String convertToJsonUsingGson(ArrayList<String> list) {
    Gson gson = new Gson();
    String userComment = gson.toJson(list);
    return userComment;
  }
    
    //requst parameter was not specified by client
  public String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
