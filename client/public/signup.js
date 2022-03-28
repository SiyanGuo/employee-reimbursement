let signupBtn = document.querySelector('#signup-btn');
let passwordShow = document.querySelector('#togglePassword');
let passwordInput = document.querySelector('#password');

passwordShow.addEventListener('click', () => {

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        passwordShow.classList.remove("fa-eye-slash");
        passwordShow.classList.add("fa-eye");
    } else {
        passwordInput.type = "password";
        passwordShow.classList.remove("fa-eye");
        passwordShow.classList.add("fa-eye-slash");
    }
});

signupBtn.addEventListener('click', async () => {
    event.preventDefault();
    let usernameInput = document.querySelector('#username');
    let passwordInput = document.querySelector('#password');
    let firstNameInput = document.querySelector('#first-name');
    let lastNameInput = document.querySelector('#last-name');
    let emailInput = document.querySelector('#email');
    let roleInput = document.querySelector('#role');
    const URL = 'http://localhost:8081/signup';

    const jsonString = JSON.stringify({
        "username": usernameInput.value,
        "password": passwordInput.value,
        "firstName": firstNameInput.value,
        "lastName": lastNameInput.value,
        "email": emailInput.value,
        "userRole": roleInput.value
    });

    let res = await fetch(URL, {
        method: 'POST',
        body: jsonString,
    });

    if (res.ok) {

        let token = res.headers.get('Token');
        localStorage.setItem('jwt', token);

        let user = await res.json();
        localStorage.setItem('first_name', user.firstName);
        localStorage.setItem('user_id', user.id);

        if (user.userRole === 'EMPLOYEE') {
            window.location = '/public/employee-home.html';
        } else if (user.userRole === 'FINANCE MANAGER') {
            window.location = '/public/manager-home.html';
        }
    } else {
        let errorMsg = await res.text();
        console.log(errorMsg);

        let errorElement = document.querySelector('#error-msg');
        errorElement.innerText = errorMsg;
    }
});