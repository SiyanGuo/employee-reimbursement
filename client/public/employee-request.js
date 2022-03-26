let firstName = document.querySelector('#first-name');
let logoutBtn = document.querySelector('.logout-btn');
let homeBtn = document.querySelector('.home-btn');
let uploadBtn = document.querySelector('#upload-btn');
let receiptFile = document.querySelector('#receipt') ;
let uploadMsg = document.querySelector('#upload-message')



firstName.innerText = localStorage.getItem('first_name');




uploadBtn.addEventListener('click', async ()=>{
    const formData = new FormData();

    formData.append('image', receiptFile.files[0]);
    const URL = 'http://localhost:8081/reimbursements/image-upload';

    let res = await fetch(URL, {
        method: 'POST',
        body: formData,
    })

    if(res.status==200) {
        uploadBtn.innerText='Uploaded!';
        uploadBtn.classList.add('bg-tahiti-blue');
        uploadBtn.classList.remove('bg-metal');
    } else {
        let errorMsg = await res.text();
        console.log(errorMsg);
        uploadMsg.innerText = errorMsg;
    }

});


homeBtn.addEventListener('click', ()=>{
    window.location = '/public/employee-home.html';
});


logoutBtn.addEventListener('click', () => {
    localStorage.removeItem('jwt');
    localStorage.removeItem('user_id');
    localStorage.removeItem('first_name');
    window.location = '/public/index.html';
});