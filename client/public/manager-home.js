let mobileBtn = document.querySelector('#mobile-btn');
let mobileMenu = document.querySelector('#mobile-menu');
let logoutBtn = document.querySelector('.logout-btn');
let reimbursementCtn = document.querySelector('#reimbursement-container')
let firstName = document.querySelector('#first-name');
let countEl=document.querySelector('#num-ofunresolved');
let responseMsg


async function populateReimbursements() {

    firstName.innerText = localStorage.getItem('first_name');
    const URL = 'http://35.225.66.206:8081/reimbursements';

    let res = await fetch(URL, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('jwt')}`
        }
    })

    if (res.ok) {

        let allReimbursements = await res.json();
        let reimbursements = allReimbursements.filter(e => { return e.status == "PENDING" });
        countEl.innerText=reimbursements.length;
        for (let reimbursement of reimbursements) {

            let card = document.createElement('div');
            card.classList.add('bg-white', 'rounded-lg', 'px-4', 'py-4', 'shadow-lg');

            let status = document.createElement('h2');
            status.classList.add('tracking-widest', 'text-xl', 'pb-3', 'font-semibold', 'text-bubble-gum');
            status.innerText = reimbursement.status;

            let id = document.createElement('h2');
            id.classList.add('tracking-widest', 'pb-3');
            id.innerText = `Id: ${reimbursement.id}`;

            let amount = document.createElement('h2');
            amount.classList.add('tracking-widest', 'pb-3');
            amount.innerText = `Amount: $${reimbursement.amount}`;

            let type = document.createElement('h2');
            type.classList.add('tracking-widest', 'pb-3');
            type.innerText = `Type: ${reimbursement.type}`;

            let description = document.createElement('h2');
            description.classList.add('tracking-widest', 'pb-3');
            description.innerText = `Description: ${reimbursement.description}`;

            let author = document.createElement('h2');
            author.classList.add('tracking-widest', 'pb-3');
            author.innerText = `Submitted by: ${reimbursement.author.firstName} ${reimbursement.author.lastName}`

            let submitAt = document.createElement('h2');
            submitAt.classList.add('tracking-widest', 'pb-3');
            let time = new Date(reimbursement.submittedAt).toLocaleDateString('en-us', { weekday: "long", year: "numeric", month: "short", day: "numeric" })
            submitAt.innerText = `Submitted at: ${time}`;

            let receipt = document.createElement('a');
            receipt.classList.add('font-semibold', 'pb-3', 'hover:text-tahiti-blue');
            receipt.setAttribute('href', `${reimbursement.receipt}`);
            receipt.setAttribute('target', '_blank');
            receipt.innerText = "View Receipt";

            let buttons = document.createElement('div');
            buttons.classList.add('flex', 'justify-around', 'my-5');

            let aprBtn = document.createElement('button');
            aprBtn.classList.add('px-4', 'py-1', 'border-cool-white', 'border-2', 'hover:bg-cool-white', 'rounded', 'resolve-btn');
            aprBtn.setAttribute('data-id', reimbursement.id);
            aprBtn.setAttribute('data-resolve', 'approved');
            aprBtn.innerText = 'Approve';

            let denyBtn = document.createElement('button');
            denyBtn.classList.add('px-4', 'py-1', 'border-cool-white', 'border-2', 'hover:bg-cool-white', 'rounded', 'resolve-btn');
            denyBtn.setAttribute('data-id', reimbursement.id);
            denyBtn.setAttribute('data-resolve', 'denied');
            denyBtn.innerText = 'Deny';

            responseMsg = document.createElement('span');
            responseMsg.classList.add('font-semibold', 'text-bubble-gum', `message-${reimbursement.id}`);

            buttons.appendChild(aprBtn);
            buttons.appendChild(denyBtn);

            card.appendChild(status);
            card.appendChild(id);
            card.appendChild(amount);
            card.appendChild(type);
            card.appendChild(description);
            card.appendChild(author);
            card.appendChild(submitAt);
            card.appendChild(receipt);
            card.appendChild(buttons)
            card.appendChild(responseMsg);
            reimbursementCtn.appendChild(card);
        }
        
    } else {
        let errorMsg = await res.text();
        console.log(errorMsg);

        let errorElement = document.querySelector('#error-msg');
        errorElement.innerText = errorMsg;
    }

};


document.addEventListener('click', async function(e){
    if(e.target && e.target.classList.contains('resolve-btn')){
        let reimbursementId = e.target.getAttribute('data-id');
        let decision = e.target.getAttribute('data-resolve');

        const formData = new FormData();
    
        formData.append('status', decision);
        const URL = `http://35.225.66.206:8081/reimbursements/${reimbursementId}`;
    
        let res = await fetch(URL, {
            method: 'PATCH',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwt')}`
            },
            body: formData,
        })

        if(res.ok){
            let responseMsgEl = document.querySelector(`.message-${reimbursementId}`)
            responseMsgEl.innerText="Thank you! The request has been resolved.";
            setTimeout(function(){ location.reload(); }, 3000);
        }else {
            let errorMsg = await res.text();
            console.log(errorMsg);
            responseMsg.innerText= errorMsg;
        }
     }
 });

mobileBtn.addEventListener("click", () => {
    mobileMenu.classList.toggle("hidden");
})

logoutBtn.addEventListener('click', () => {
    localStorage.removeItem('jwt');
    localStorage.removeItem('user_id');
    localStorage.removeItem('first_name');
    window.location = '/index.html';
});


window.addEventListener('load', () => {
    if (localStorage.getItem('jwt') == null) {
        window.location = '/forbidden.html'
    }
    populateReimbursements();
});

