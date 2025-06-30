// Load available cars from backend and display them
function loadCars() {
    fetch('/api/cars')
        .then(res => res.json())
        .then(cars => {
            const carList = document.querySelector('.car-list');
            carList.innerHTML = '';
            cars.forEach(car => {
                const div = document.createElement('div');
                div.className = 'car';
                div.innerHTML = `
                    <h3>${car.brand} ${car.model}</h3>
                    <p>Price: ₹${car.basePricePerDay}/day</p>
                    <p>Status: ${car.available ? 'Available' : 'Not Available'}</p>
                    ${car.available ? `<button onclick="showRentForm('${car.carId}', '${car.brand}', '${car.model}', ${car.basePricePerDay})">Rent</button>` : ''}
                `;
                carList.appendChild(div);
            });
        });
}

function showRentForm(carId, brand, model, price) {
    const name = prompt(`Enter your name to rent ${brand} ${model} (₹${price}/day):`);
    if (!name) return;
    const days = prompt(`How many days do you want to rent it?`);
    if (!days || isNaN(days)) return alert("Invalid number of days.");

    fetch('/api/rent', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ carId, customerName: name, rentalDays: parseInt(days) })
    })
    .then(res => res.text())
    .then(msg => {
        alert(msg);
        loadCars();
    });
}

function returnCar() {
    const carId = prompt("Enter the Car ID to return:");
    if (!carId) return;
    fetch(`/api/return?carId=${encodeURIComponent(carId)}`, {
        method: 'POST'
    })
    .then(res => res.text())
    .then(msg => {
        alert(msg);
        loadCars();
    });
}

window.onload = loadCars;
