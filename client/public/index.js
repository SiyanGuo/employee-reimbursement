let loginBtn = document.querySelector('#login-btn');

loginBtn.addEventListener('click', async () => {
    event.preventDefault();
    let usernameInput = document.querySelector('#username');
    let passwordInput = document.querySelector('#password');

    const URL = 'http://localhost:8081/login';

    const jsonString = JSON.stringify({
        "username": usernameInput.value,
        "password": passwordInput.value
    });

    let res = await fetch(URL, {
        method: 'POST',
        body: jsonString,
    });

    let token = res.headers.get('Token');
    localStorage.setItem('jwt', token);

    if (res.status === 200) {
        let user = await res.json();

        localStorage.setItem('user_id', user.id); 
        if (user.userRole === 'EMPLOYEE') {
            window.location = '/public/employee-page.html';
        } else if (user.userRole === 'MANAGER') {
            window.location = '/public/manager-page.html';
        }
    } else {
        let errorMsg = await res.text();
        console.log(errorMsg);

        let errorElement = document.querySelector('#error-msg');
        errorElement.innerText = errorMsg;
    }
});