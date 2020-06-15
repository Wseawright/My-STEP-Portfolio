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
function getData() {
  fetch('/data').then(response => response.text()).then((txt) => {
    document.getElementById('data-container').innerText = txt;
  });
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
    loadUser();   
    createMap(); 
    fetchBlobstoreUrlAndShowForm();
}