// Declare variable
var author = '', account = {};

var url = window.location.href.split('/');
url.pop();
url.pop();
var path = url.join('/') + '/';

// Get param
function GetURLParameter(sParam) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) {
            return sParameterName[1];
        }
    }
    return -1;
}

// Authorization
function authorization() {
    var http = new XMLHttpRequest();

    http.open('GET', path + `api/authorization`, true);

    http.send();

    http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var responseData = JSON.parse(this.responseText);
            author = responseData.data;
            console.log(author);
            if(author == 'ADMIN') {
                var bodyDOM = document.querySelector('body');
            
                var btnGoToAdmin = document.createElement('a');
                btnGoToAdmin.classList.add('go-to-admin');
                btnGoToAdmin.href = `${path}home`;
                btnGoToAdmin.innerHTML = `
                <i class="fas fa-shopping-bag"></i>
                <p>Quay về trang mua sắm</p>
                `;
                
                bodyDOM.appendChild(btnGoToAdmin);
            }
            else if(author == 'STAFF') {
				document.querySelectorAll('#tab-admin').forEach(x => {
					x.style.display = 'none';
				})
            }
        }
    }
}

// Authentication
function getCurrentAccount() {
    var http = new XMLHttpRequest();

    http.open('GET', path + `api/current-account`, true);

    http.send();

    http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var responseData = JSON.parse(this.responseText);
            account = responseData.data;
            loadHeader();
        }
    }
}

function loadHeader() {
	var fullNameDOM = document.querySelector('body > div.app > div > div.grid.wide > div > div.col.l-3.m-12.c-12 > div > div > div.app-container-menu-user__info > div.app-container-menu-user-info__name');
    var emailDOM = document.querySelector('body > div.app > div > div.grid.wide > div > div.col.l-3.m-12.c-12 > div > div > div.app-container-menu-user__info > div.app-container-menu-user-info__email');
    fullNameDOM.innerHTML = account.fullName;
    emailDOM.innerHTML = account.email;
}

// Format Item's id
function formatItemID(id) {
    var tmp = id;
    var count = 0;
    while (tmp > 0) {
        count++;
        tmp = (tmp - tmp % 10) / 10;
    }
    while (4 - count > 0) {
        id = '0' + id;
        count++;
    }
    return id;
}

// Call function
authorization();
getCurrentAccount();