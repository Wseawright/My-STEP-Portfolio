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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  public class Task {
    public String commentInput;
    public long timestamp;
    public String name;

    public Task(String name, String commentInput, long timestamp) {
        this.commentInput = commentInput;
        this.timestamp = timestamp;
        this.name = name;
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String commentInput = getParameter(request, "commentInput", "");
    String name = getParameter(request, "name", "");
    long timestamp = System.currentTimeMillis();
    
    Entity commentEntity = new Entity("Comments");
    commentEntity.setProperty("timestamp", timestamp);
    commentEntity.setProperty("commentInput", commentInput);
    commentEntity.setProperty("name", name);
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

// load comments code
    Query query = new Query("Comments").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    
    List<Task> comments = new ArrayList<>();
    for (Entity Comment : results.asIterable()) {
        commentEntity.getProperty("timestamp");
        commentEntity.getProperty("commentInput");
        commentEntity.getProperty("name");
    
    Task comment = new Task(name, commentInput, timestamp);
    comments.add(comment);
   }
    
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
   }

 public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // Must be logged in to post comments
    String commentInput = request.getParameter("commentInput");
    String name = request.getParameter("name");
    long timestamp = System.currentTimeMillis();
    String email = userService.getCurrentUser().getEmail();
    String displayemail;
    
    if (request.getParameter("displayemail") == null) {
      displayemail = "off";
    } else {
      displayemail = "on";
    }

    Entity commentEntity = new Entity("Comments");
    commentEntity.setProperty("commentInput", commentInput);
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("timestamp", timestamp);
    commentEntity.setProperty("email", email);
    commentEntity.setProperty("displayemail", displayemail);
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");
    
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

