let mobileBtn = document.querySelector('#mobile-btn');
let mobileMenu = document.querySelector('#mobile-menu');
let logoutBtn = document.querySelector('.logout-btn');
let reimbursementCtn = document.querySelector('#reimbursement-container')

logoutBtn.addEventListener('click', () => {
    localStorage.removeItem('jwt');

    window.location = '/public/index.html';
});


window.addEventListener('load', (event) => {
    if(localStorage.getItem('jwt')==null){
        window.location = '/public/forbidden.html'
    }
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

            let card = document.createElement('div');
            card.classList.add('bg-white', 'rounded-lg', 'px-4', 'py-4', 'drop-shadow-md');

            let status = document.createElement('h2');
            status.classList.add('tracking-widest', 'text-xl', 'pb-3', 'font-semibold');
            status.innerText= reimbursement.status;

            let id= document.createElement('h2');
            id.classList.add('tracking-widest', 'text-purple');
            id.innerText = `Id: ${reimbursement.id}`;

            let amount= document.createElement('h2');
            amount.classList.add('tracking-widest', 'text-purple');
            amount.innerText = `Amount: ${reimbursement.amount}`;

            let type= document.createElement('h2');
            type.classList.add('tracking-widest', 'text-purple');
            type.innerText = `Type: ${reimbursement.type}`;

            let description= document.createElement('h2');
            description.classList.add('tracking-widest', 'text-purple');
            description.innerText = `Description: ${reimbursement.description}`;

            let submitAt= document.createElement('h2');
            submitAt.classList.add('tracking-widest', 'text-purple');
            submitAt.innerText = `Submitted at: ${reimbursement.submittedAt}`;

            let resolvedAt= document.createElement('h2');
            resolvedAt.classList.add('tracking-widest', 'text-purple');
            
            let resolvedBy= document.createElement('h2');
            resolvedBy.classList.add('tracking-widest', 'text-purple');
            
            let receipt = document.createElement('a');
            receipt.classList.add('font-semibold', 'text-purple', 'hover:text-bubble-gum');
            receipt.setAttribute('href', `${reimbursement.receipt}`);
            receipt.setAttribute('target', '_blank');
            receipt.innerText= "View Receipt";
       
            if(reimbursement.status == 'APPROVED'){
                status.classList.add('text-purple');
                resolvedAt.innerText = `Resolved at: ${reimbursement.resolvedAt}`
                resolvedBy.innerText = `Resolved by: ${reimbursement.resolver.firstName} ${reimbursement.resolver.lastName}`
            } else if (reimbursement.status == 'PENDING') {
                status.classList.add('text-bubble-gum');
                resolvedAt.innerText = "PENDING";
                resolvedBy.innerText = "PENDING";
            } else {
                status.classList.add('text-tahiti-blue');
                resolvedAt.innerText = `Resolved at: ${reimbursement.resolvedAt}`
                resolvedBy.innerText = `Resolved by: ${reimbursement.resolver.firstName} ${reimbursement.resolver.lastName}`;
            }

            card.appendChild(status);
            card.appendChild(id);
            card.appendChild(amount);
            card.appendChild(type);
            card.appendChild(description);
            card.appendChild(submitAt);
            card.appendChild(resolvedAt);
            card.appendChild(resolvedBy)
            card.appendChild(receipt);

            reimbursementCtn.appendChild(card);
       
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

