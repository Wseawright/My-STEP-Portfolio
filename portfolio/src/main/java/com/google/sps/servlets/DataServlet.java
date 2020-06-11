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
    public long id;
    public String email;
    public String displayemail;

    public Task(long id, String commentInput, long timestamp, String name, String email, String displayemail) {
        this.commentInput = commentInput;
        this.timestamp = timestamp;
        this.name = name;
        this.id = id;
        this.email = email;
        this.displayemail = displayemail;
    }
  }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Query query = new Query("Task").addSort("timestamp", SortDirection.DESCENDING);
    
    // DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    
    List<Task> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        long id = entity.getKey().getId();
        String commentInput = (String) entity.getProperty("commentInput");
        long timestamp = (long) entity.getProperty("timestamp");
        String name = (String) entity.getProperty("name");
        String email = (String) entity.getProperty("email");
        String displayemail = (String) entity.getProperty("displayemail");
    Task comment = new Task(id, commentInput, timestamp, name, email, displayemail);
    comments.add(comment);
   }
    
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
   }

  private String convertToJsonUsingGson(ArrayList<String> lst) {
    Gson gson = new Gson();
    String json = gson.toJson(lst);
    return json;
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();

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

