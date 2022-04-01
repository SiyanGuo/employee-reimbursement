let signupBtn = document.querySelector('#signup-btn');
let passwordShow = document.querySelector('#togglePassword');
let passwordInput = document.querySelector('#password');
let signupForm = document.querySelector('#signup-form');

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

signupForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    let usernameInput = document.querySelector('#username');
    let passwordInput = document.querySelector('#password');
    let firstNameInput = document.querySelector('#first-name');
    let lastNameInput = document.querySelector('#last-name');
    let emailInput = document.querySelector('#email');
    let roleInput = document.querySelector('#role');

    const formData = new FormData();
    formData.append("username", usernameInput.value);
    formData.append("password", passwordInput.value);
    formData.append("firstName", firstNameInput.value);
    formData.append("lastName", lastNameInput.value);
    formData.append("email", emailInput.value);
    formData.append("userRole", roleInput.value);

    const URL = 'http://35.225.66.206:8081/signup';

    let res = await fetch(URL, {
        method: 'POST',
        body: formData,
    });

    if (res.ok) {

        let token = res.headers.get('Token');
        localStorage.setItem('jwt', token);

        let user = await res.json();
        localStorage.setItem('first_name', user.firstName);
        localStorage.setItem('user_id', user.id);

        if (user.userRole === 'EMPLOYEE') {
            window.location.assign('/employee-home.html');
        } else if (user.userRole === 'FINANCE MANAGER') {
            window.location.assign('/manager-home.html')
        }
    } else {
        let errorMsg = await res.text();
        console.log(errorMsg);

        let errorElement = document.querySelector('#error-msg');
        errorElement.innerText = errorMsg;
    }
});