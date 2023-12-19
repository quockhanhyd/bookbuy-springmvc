// Lấy tham chiếu đến input
var fromDateDOM = document.getElementById('from-date');
var toDateDOM = document.getElementById('to-date');
var typeDOM = document.getElementById('type');

// Tạo một đối tượng Date mới để lấy ngày hiện tại
var today = new Date();

// Format ngày thành yyyy-mm-dd để gán vào input
var dd = String(today.getDate()).padStart(2, '0');
var mm = String(today.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
var yyyy = today.getFullYear();

var beginDateMonth = yyyy + '-' + mm + '-' + '01';
var currentDate = yyyy + '-' + mm + '-' + dd;

// Gán giá trị mặc định cho input
fromDateDOM.value = beginDateMonth;
toDateDOM.value = currentDate;

//
var dataResult1 = '';
var dataResult2 = '';
getData();

// Get data
function getData() {
    var http = new XMLHttpRequest();

    http.open('POST', path + 'admin/api/statisticBook');
    http.setRequestHeader('Content-Type', 'application/json');
    http.send(JSON.stringify({fromDate: fromDateDOM.value, toDate: toDateDOM.value, type: typeDOM.value }));

    http.onreadystatechange = function() {
        if(this.readyState == 4 && this.status == 200) {
            var responseData = JSON.parse(this.responseText);
            console.log(responseData);
            dataResult1 = responseData.data.ltData1;
            dataResult2 = responseData.data.ltData2;
            
            google.charts.load('current', {'packages':['corechart']});
			google.charts.setOnLoadCallback(drawChart);
        }
    }
}

function drawChart() {
	
    var data = google.visualization.arrayToDataTable([
	    ['Tên sách', 'Số lượng đã bán'],
	    ...dataResult1.map(item => [item.name, item.numberSold])
	]);

    var options = {
      title: 'My Daily Activities'
    };

    var chart = new google.visualization.PieChart(document.getElementById('piechart'));

    chart.draw(data, options);
    
    // --------------
    
    var data2 = google.visualization.arrayToDataTable([
		[typeDOM.value == 1 ? 'Ngày' : typeDOM.value == 2 ? 'Tháng' : typeDOM.value == 3 ? 'Quý' : 'Năm', 'Doanh thu'],
		...dataResult2.map(item => [formatDate(new Date(item.date)), item.sale])
	]);

        var options2 = {
          title: 'Company Performance',
          hAxis: {title: 'Year',  titleTextStyle: {color: '#333'}},
          vAxis: {minValue: 0}
        };

        var chart2 = new google.visualization.AreaChart(document.getElementById('chart_div'));
        chart2.draw(data2, options2);
  }
  
function formatDate(date) {
	let day = date.getDate().toString().padStart(2, '0'); // Lấy ngày, thêm số 0 nếu cần
	let month = (date.getMonth() + 1).toString().padStart(2, '0'); // Lấy tháng (tháng bắt đầu từ 0), thêm số 0 nếu cần
	let year = date.getFullYear().toString(); // Lấy năm
	
	return `${day}/${month}/${year}`;
}
  