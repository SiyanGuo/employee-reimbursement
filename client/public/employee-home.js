let mobileBtn = document.querySelector('#mobile-btn');
let mobileMenu = document.querySelector('#mobile-menu');
let logoutBtn = document.querySelector('.logout-btn');
let reimbursementCtn = document.querySelector('#reimbursement-container')
let firstName = document.querySelector('#first-name');
let statusFilter = document.querySelector('#r-status');
let filterMsg = document.querySelector('#filter-msg');

let reimbursements = [];
async function fetchReimbursements() {

    firstName.innerText = localStorage.getItem('first_name');
    const URL = `http://35.225.66.206:8081/users/${localStorage.getItem('user_id')}/reimbursements`;

    let res = await fetch(URL, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('jwt')}`
        }
    })

    if (res.ok) {

        reimbursements = await res.json();
        filterMsg.innerText = reimbursements.length;
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
        card.classList.add('bg-white', 'rounded-lg', 'px-4', 'py-4', 'drop-shadow-md');

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

        let submitAt = document.createElement('h2');
        submitAt.classList.add('tracking-widest', 'pb-3');
        let date = new Date(reimbursement.submittedAt).toLocaleDateString('en-us', { weekday: "long", year: "numeric", month: "short", day: "numeric" });
        let time = new Date(reimbursement.submittedAt).toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute: '2-digit' });
        submitAt.innerText = `Submitted at: ${date} ${time}`;

        let resolvedAt = document.createElement('h2');
        resolvedAt.classList.add('tracking-widest', 'pb-3');

        let resolvedBy = document.createElement('h2');
        resolvedBy.classList.add('tracking-widest', 'pb-3');

        let receipt = document.createElement('a');
        receipt.classList.add('font-semibold', 'hover:text-tahiti-blue', 'pb-3');
        receipt.setAttribute('href', `${reimbursement.receipt}`);
        receipt.setAttribute('target', '_blank');
        receipt.innerText = "View Receipt";

        if (reimbursement.status == 'APPROVED') {
            status.classList.add('text-purple');
           
            let date2 = new Date(reimbursement.resolvedAt).toLocaleDateString('en-us', { weekday: "long", year: "numeric", month: "short", day: "numeric" });
            let time2 = new Date(reimbursement.resolvedAt).toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute: '2-digit' });

            resolvedAt.innerText = `Resolved at: ${date2} ${time2}`;
            resolvedBy.innerText = `Resolved by: ${reimbursement.resolver.firstName} ${reimbursement.resolver.lastName}`;
        } else if (reimbursement.status == 'PENDING') {
            status.classList.add('text-bubble-gum');
            resolvedAt.innerText = "Resolved at: PENDING";
            resolvedBy.innerText = "Resolved by: PENDING";
        } else {
            status.classList.add('text-tahiti-blue');

            let date3 = new Date(reimbursement.resolvedAt).toLocaleDateString('en-us', { weekday: "long", year: "numeric", month: "short", day: "numeric" });
            let time3 = new Date(reimbursement.resolvedAt).toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute: '2-digit' });

            resolvedAt.innerText = `Resolved at: ${date3} ${time3}`;
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
}


statusFilter.addEventListener('change', (event) => {
    reimbursementCtn.innerHTML = "";
    if (event.target.value === "all") {
        filterMsg.innerText = reimbursements.length;
        populateReimbursements(reimbursements);
    } else {
        let filteredReimbursements = reimbursements.filter(e => { return e.status === event.target.value });
        filterMsg.innerText = filteredReimbursements.length + " " + event.target.value.toLowerCase();
        populateReimbursements(filteredReimbursements);
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


window.addEventListener('load', (event) => {
    if (localStorage.getItem('jwt') == null) {
        window.location = '/forbidden.html';
    }
    fetchReimbursements();
});

