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

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

// function getData() {
//   fetch('/data').then(response => response.text()).then((data) => {
//     document.getElementById('data-container').innerText = data;
//   });
// }

async function getData() {
  console.log('Getting Data');
  const response = await fetch('/data');
  const data = await response.text();
  console.log(data)
  document.getElementById('data-container').innerText = data;
}

function loadComments() {
  const commentCount = document.getElementById('maxcomments');
  console.log(commentCount.value)
  fetch('/data').then(response => response.json()).then((comments) => {
    const commentListElement = document.getElementById('comment-list');
    comments.forEach((comment) => {
      console.log(comment.commentInput)
      commentListElement.appendChild(createCommentElement(comment));
    })
  });
}

function getMessages() {
  const commentCount = document.getElementById('maxcomments');
  console.log(commentCount.name)
  document.getElementById('comment-list').innerHTML = "";
  fetch('/data?maxcomments=' + commentCount.value).then(response => response.json()).then((comments) => {
    const commentListElement = document.getElementById('comment-list');
    comments.forEach((comment) => {
      console.log(comment.commentInput)
      commentListElement.appendChild(createCommentElement(comment));
    })
  });
}

function sortComments() {
  const sort = document.getElementById('sort');
  console.log(sort.value)
  document.getElementById('comment-list').innerHTML = "";
  fetch('/data?sort=' + sort.value).then(response => response.json()).then((comments) => {
    const commentListElement = document.getElementById('comment-list');
    comments.forEach((comment) => {
      console.log(comment.commentInput)
      commentListElement.appendChild(createCommentElement(comment));
    })
  });
}

function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment collection-item';

  const commentInputElement = document.createElement('span');
  commentInputElement.innerText = comment.commentInput;

  const nameElement = document.createElement('span');
  if (comment.name === undefined || comment.name === "") {
    nameElement.innerHTML = "-- Anonymous".italics().bold();
  } else {
    nameElement.innerHTML = ("--" + comment.name).italics().bold();
  }
  nameElement.style.marginLeft = "15px"

  const emailElement = document.createElement('span');
  if (comment.displayemail === "on") {
    emailElement.innerHTML = "(" + comment.email + ")";
  } else {
    emailElement.innerHTML = "(Hidden email)"
  }
  emailElement.style.margin = "2px";

  const timeElement = document.createElement('span');
  var date = new Date(comment.timestamp);
  timeElement.innerText = date.toString().slice(0,24);
  timeElement.style.float = "right";
  timeElement.style.marginRight = "10px";

  var deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.style.float = "right";
  deleteButtonElement.addEventListener('click', () => {
    deleteComment(comment);

    // Remove the comment from the DOM.
    commentElement.remove();
  });

  commentElement.appendChild(commentInputElement);
  commentElement.appendChild(nameElement);
  commentElement.appendChild(emailElement);
  commentElement.appendChild(deleteButtonElement);
  commentElement.appendChild(timeElement);
  return commentElement;
}

function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete', {method: 'POST', body: params});
}



function loadUser(){
    fetch('/loginStatus').then(response => response.text()).then((txt) => {
    const loginElement = document.getElementById('login');
    console.log(txt)
    loginElement.innerHTML = txt;
    var form = document.getElementById("commentSection");
    if (txt.includes("You")) {
      form.style.display = "none";
      document.getElementById("login").innerHTML = "<i>" + txt + "</i>";
    } else{
      document.getElementById("login").innerHTML = "<i>" + txt + "</i>";
    }});
}

function fetchBlobstoreUrlAndShowForm() {
  fetch('/blobstore-upload').then((response) => {
        return response.text();
      })
      .then((imageUploadUrl) => {
        const messageForm = document.getElementById('file');
        messageForm.action = imageUploadUrl;
        messageForm.classList.remove('hidden');
      });
}

function drawChart() {
  const data = new google.visualization.DataTable();
  data.addColumn('string', 'instrument');
  data.addColumn('number', 'playability');
        data.addRows([
          ['Mellophone', 10],
          ['French Horn', 5],
          ['Trumpet', 15]
        ]);
  const options = {
    'title': 'Instruments I know how to play',
    'width':400,
    'height':300
  };

  const chart = new google.visualization.PieChart(
      document.getElementById('chart-container'));
      chart.draw(data, options);
}

function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 42.3627196, lng: -71.0871065}, zoom: 16});
}


function loadPage(){
    getData();
    loadComments();
    loadUser();   
    createMap(); 
    fetchBlobstoreUrlAndShowForm();
    // getMessages();
}