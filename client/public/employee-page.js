let mobileBtn = document.querySelector('#mobile-btn');
let mobileMenu = document.querySelector('#mobile-menu');
let logoutBtn = document.querySelector('.logout-btn');


logoutBtn.addEventListener('click', () => {
    localStorage.removeItem('jwt');

    window.location = '/public/index.html';
});


window.addEventListener('load', (event) => {
    populateReimbursements();
});

async function populateReimbursements() {
    const URL = `http://localhost:8081/users/${localStorage.getItem('user_id')}/reimbursements`;

    let res = await fetch(URL, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('jwt')}`
        }
    })

    if (res.status == 200) {

        let reimbursements = await res.json();

        for (let reimbursement of reimbursements) {

        }


    } else {
        let errorMsg = await res.text();
        console.log(errorMsg);

        let errorElement = document.querySelector('#error-msg');
        errorElement.innerText = errorMsg;
    }

};

mobileBtn.addEventListener("click", () => {
    mobileMenu.classList.toggle("hidden");
})

