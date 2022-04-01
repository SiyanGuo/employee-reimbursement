let mobileBtn = document.querySelector('#mobile-btn');
let mobileMenu = document.querySelector('#mobile-menu');
let logoutBtn = document.querySelector('.logout-btn');
let reimbursementCtn = document.querySelector('#reimbursement-container')
let countEl = document.querySelector('#num-of-requests');
let allBtn = document.querySelector('.view-all');
let deniedBtn = document.querySelector('.view-denied');
let approvedBtn = document.querySelector('.view-approved');
let reimbursements = [];

async function fetchReimbursements() {

    const URL = 'http://localhost:8081/reimbursements';

    let res = await fetch(URL, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('jwt')}`
        }
    })

    if (res.ok) {

        let data = await res.json();
        reimbursements = data.filter(e => { return e.status !== "PENDING" });

        countEl.innerText = reimbursements.length+" resolved";
        populateReimbursements(reimbursements);

    } else {
        let errorMsg = await res.text();
        console.log(errorMsg);

        let errorElement = document.querySelector('#error-msg');
        errorElement.innerText = errorMsg;
    }

};


function populateReimbursements(reimbursements) {

    for (let reimbursement of reimbursements) {

        let card = document.createElement('div');
        card.classList.add('bg-white', 'rounded-lg', 'px-4', 'py-4', 'shadow-lg');

        let status = document.createElement('h2');
        status.classList.add('tracking-widest', 'text-xl', 'pb-3', 'font-semibold');
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
        author.innerText = `Submitted by: ${reimbursement.author.firstName} ${reimbursement.author.lastName}`;

        let submitAt = document.createElement('h2');
        submitAt.classList.add('tracking-widest', 'pb-3');
        let date = new Date(reimbursement.submittedAt).toLocaleDateString('en-us', { weekday: "long", year: "numeric", month: "short", day: "numeric" });
        let time = new Date(reimbursement.submittedAt).toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute: '2-digit' });
        submitAt.innerText = `Submitted at: ${date} ${time}`;

        let resolvedAt = document.createElement('h2');
        resolvedAt.classList.add('tracking-widest', 'pb-3');
        let date2 = new Date(reimbursement.resolvedAt).toLocaleDateString('en-us', { weekday: "long", year: "numeric", month: "short", day: "numeric" });
        let time2 = new Date(reimbursement.resolvedAt).toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute: '2-digit' });

        resolvedAt.innerText = `Resolved at: ${date2} ${time2}`;

        let resolvedBy = document.createElement('h2');
        resolvedBy.classList.add('tracking-widest', 'pb-3');
        resolvedBy.innerText = `Resolved by: ${reimbursement.resolver.firstName} ${reimbursement.resolver.lastName}`;

        let receipt = document.createElement('a');
        receipt.classList.add('font-semibold', 'pb-3', 'hover:text-tahiti-blue');
        receipt.setAttribute('href', `${reimbursement.receipt}`);
        receipt.setAttribute('target', '_blank');
        receipt.innerText = "View Receipt";

        reimbursement.status == 'DENIED' ? status.classList.add('text-tahiti-blue') : status.classList.add('text-purple');

        card.appendChild(status);
        card.appendChild(id);
        card.appendChild(amount);
        card.appendChild(type);
        card.appendChild(description);
        card.appendChild(author);
        card.appendChild(submitAt);
        card.appendChild(resolvedAt);
        card.appendChild(resolvedBy);
        card.appendChild(receipt);
        reimbursementCtn.appendChild(card);
    };
}


allBtn.addEventListener("click", () => {
    reimbursementCtn.innerHTML = "";
    countEl.innerText = reimbursements.length + " resolved";
    populateReimbursements(reimbursements);
});

deniedBtn.addEventListener("click", ()=>{
    reimbursementCtn.innerHTML = "";
    let deniedReimbursements = reimbursements.filter(e => { return e.status === "DENIED" });
    countEl.innerText = deniedReimbursements.length + " denied";
    populateReimbursements(deniedReimbursements);
})

approvedBtn.addEventListener("click", ()=>{
    reimbursementCtn.innerHTML = "";
    let approvedReimbursements = reimbursements.filter(e => { return e.status === "APPROVED" });
    countEl.innerText = approvedReimbursements.length + " approved";
    populateReimbursements(approvedReimbursements);
})


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
        window.location = '/forbidden.html';
    }
    fetchReimbursements();
});

