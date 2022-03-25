let loginBtn = document.querySelector('#login-btn');
let passwordShow = document.querySelector('#togglePassword');
let passwordInput = document.querySelector('#password');

passwordShow.addEventListener('click', ()=>{

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



    if (res.status === 200) {

        let token = res.headers.get('Token');
        localStorage.setItem('jwt', token);
       
        let user = await res.json();
        localStorage.setItem('firstName', user.firstName);
        localStorage.setItem('user_id', user.id); 

        if (user.userRole === 'EMPLOYEE') {
            window.location = '/public/employee-home.html';
        } else if (user.userRole === 'MANAGER') {
            window.location = '/public/manager-home.html';
        }
    } else {
        let errorMsg = await res.text();
        console.log(errorMsg);

        let errorElement = document.querySelector('#error-msg');
        errorElement.innerText = errorMsg;
    }
});