/**
 * 
 */




/* PIE CHART */

new Chart(document.getElementById("pie-chart"), {
    type: 'pie',
    data: {
      labels: ["South", "North", "Center"],
      datasets: [{
        label: "Patients",
        backgroundColor: ["#3e95cd", "#8e5ea2","#3cba9f"],
        data: [south,north,center]
      }]
    },
    options: {
      title: {
        display: true,
        text: 'Predicted world population (millions) in 2050'
      }
    }
});