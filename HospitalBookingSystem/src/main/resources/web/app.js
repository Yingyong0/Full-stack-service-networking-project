const API_BASE = 'http://localhost:8080/api';

// Show message
function showMessage(text, type = 'info') {
    const messageEl = document.getElementById('message');
    messageEl.textContent = text;
    messageEl.className = `message ${type}`;
    messageEl.style.display = 'block';
    setTimeout(() => {
        messageEl.style.display = 'none';
    }, 3000);
}

// Switch tabs
function showTab(tabName, event) {
    // Hide all tab content
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    
    // Remove active state from all buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Show selected tab content
    document.getElementById(tabName).classList.add('active');
    
    // Activate corresponding button
    if (event && event.target) {
        event.target.classList.add('active');
    } else {
        // If no event object, find by button text
        document.querySelectorAll('.tab-btn').forEach(btn => {
            if (btn.textContent.includes(tabName === 'patients' ? 'Patient' : 
                                          tabName === 'doctors' ? 'Doctor' : 
                                          tabName === 'appointments' ? 'Appointment' : 'Registration')) {
                btn.classList.add('active');
            }
        });
    }
    
    // Load data for corresponding tab
    if (tabName === 'doctors') {
        loadDoctors();
    } else if (tabName === 'patients') {
        loadPatients();
    } else if (tabName === 'appointments') {
        loadAppointments();
    } else if (tabName === 'registrations') {
        loadRegistrations();
    }
}

// ============ Patient Management ============
async function registerPatient(event) {
    event.preventDefault();
    
    const patient = {
        name: document.getElementById('patientName').value,
        phone: document.getElementById('patientPhone').value,
        idCard: document.getElementById('patientIdCard').value,
        gender: document.getElementById('patientGender').value,
        age: parseInt(document.getElementById('patientAge').value)
    };
    
    try {
        const response = await fetch(`${API_BASE}/patients`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(patient)
        });
        
        const data = await response.json();
        
        if (response.ok) {
            showMessage(`Patient registered successfully! Patient ID: ${data.id}`, 'success');
            document.getElementById('patientForm').reset();
            loadPatients();
        } else {
            showMessage(data.error || 'Registration failed', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    }
}

async function loadPatients() {
    try {
        const response = await fetch(`${API_BASE}/patients`);
        const patients = await response.json();
        
        const listEl = document.getElementById('patientList');
        if (patients.length === 0) {
            listEl.innerHTML = '<p>No patient data available</p>';
            return;
        }
        
        listEl.innerHTML = patients.map(patient => `
            <div class="item-card">
                <h4>${patient.name} (ID: ${patient.id})</h4>
                <p><strong>Phone:</strong> ${patient.phone}</p>
                <p><strong>ID Card:</strong> ${patient.idCard}</p>
                <p><strong>Gender:</strong> ${patient.gender}</p>
                <p><strong>Age:</strong> ${patient.age}</p>
            </div>
        `).join('');
    } catch (error) {
        showMessage('Failed to load patient list: ' + error.message, 'error');
    }
}

// ============ Doctor Management ============
async function loadDoctors() {
    try {
        const response = await fetch(`${API_BASE}/doctors`);
        const doctors = await response.json();
        
        const listEl = document.getElementById('doctorList');
        if (doctors.length === 0) {
            listEl.innerHTML = '<p>No doctor data available</p>';
            return;
        }
        
        listEl.innerHTML = doctors.map(doctor => `
            <div class="doctor-card">
                <h4>${doctor.name} (${doctor.title})</h4>
                <p><strong>Department:</strong> ${doctor.department}</p>
                <p><strong>Doctor ID:</strong> ${doctor.id}</p>
                <p><strong>Phone:</strong> ${doctor.phone}</p>
                <p><strong>Schedule:</strong> ${doctor.schedule}</p>
            </div>
        `).join('');
    } catch (error) {
        showMessage('Failed to load doctor list: ' + error.message, 'error');
    }
}

// ============ Appointment Management ============
async function createAppointment(event) {
    event.preventDefault();
    
    const patientId = document.getElementById('appointmentPatientId').value;
    const doctorId = document.getElementById('appointmentDoctorId').value;
    
    // Get patient and doctor information
    let patient, doctor;
    try {
        const patientRes = await fetch(`${API_BASE}/patients/${patientId}`);
        patient = await patientRes.json();
        
        const doctorRes = await fetch(`${API_BASE}/doctors/${doctorId}`);
        doctor = await doctorRes.json();
        
        if (!patientRes.ok || !doctorRes.ok) {
            showMessage('Patient or doctor does not exist', 'error');
            return;
        }
    } catch (error) {
        showMessage('Failed to verify patient or doctor information: ' + error.message, 'error');
        return;
    }
    
    const appointment = {
        patientId: patientId,
        patientName: patient.name,
        doctorId: doctorId,
        doctorName: doctor.name,
        department: doctor.department,
        appointmentDate: document.getElementById('appointmentDate').value,
        appointmentTime: document.getElementById('appointmentTime').value,
        status: 'Pending'
    };
    
    try {
        const response = await fetch(`${API_BASE}/appointments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(appointment)
        });
        
        const data = await response.json();
        
        if (response.ok) {
            showMessage(`Appointment created successfully! Appointment ID: ${data.id}`, 'success');
            document.getElementById('appointmentForm').reset();
            loadAppointments();
        } else {
            showMessage(data.error || 'Failed to create appointment', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    }
}

async function loadAppointments() {
    try {
        const response = await fetch(`${API_BASE}/appointments`);
        const appointments = await response.json();
        
        const listEl = document.getElementById('appointmentList');
        if (appointments.length === 0) {
            listEl.innerHTML = '<p>No appointment data available</p>';
            return;
        }
        
        listEl.innerHTML = appointments.map(appointment => `
            <div class="item-card">
                <h4>Appointment ${appointment.id} <span class="status ${getStatusClass(appointment.status)}">${appointment.status}</span></h4>
                <p><strong>Patient:</strong> ${appointment.patientName} (${appointment.patientId})</p>
                <p><strong>Doctor:</strong> ${appointment.doctorName} (${appointment.doctorId})</p>
                <p><strong>Department:</strong> ${appointment.department}</p>
                <p><strong>Appointment Date:</strong> ${appointment.appointmentDate} ${appointment.appointmentTime}</p>
                ${appointment.status === 'Pending' ? `
                    <button class="btn btn-danger" onclick="cancelAppointment('${appointment.id}')">Cancel Appointment</button>
                ` : ''}
            </div>
        `).join('');
    } catch (error) {
        showMessage('Failed to load appointment list: ' + error.message, 'error');
    }
}

async function cancelAppointment(id) {
    if (!confirm('Are you sure you want to cancel this appointment?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/appointments/${id}`, {
            method: 'DELETE'
        });
        
        const data = await response.json();
        
        if (response.ok) {
            showMessage('Appointment cancelled', 'success');
            loadAppointments();
        } else {
            showMessage(data.error || 'Failed to cancel appointment', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    }
}

// ============ Registration Management ============
async function createRegistration(event) {
    event.preventDefault();
    
    const patientId = document.getElementById('registrationPatientId').value.trim();
    const doctorId = document.getElementById('registrationDoctorId').value.trim();
    
    if (!patientId || !doctorId) {
        showMessage('Please fill in Patient ID and Doctor ID', 'error');
        return;
    }
    
    // Get patient and doctor information
    let patient, doctor;
    try {
        const patientRes = await fetch(`${API_BASE}/patients/${patientId}`);
        if (!patientRes.ok) {
            showMessage('Patient does not exist, please register patient first', 'error');
            return;
        }
        patient = await patientRes.json();
        
        const doctorRes = await fetch(`${API_BASE}/doctors/${doctorId}`);
        if (!doctorRes.ok) {
            showMessage('Doctor does not exist', 'error');
            return;
        }
        doctor = await doctorRes.json();
    } catch (error) {
        showMessage('Failed to verify patient or doctor information: ' + error.message, 'error');
        console.error('Verification error:', error);
        return;
    }
    
    const visitDate = document.getElementById('visitDate').value;
    const visitTime = document.getElementById('visitTime').value;
    const fee = parseFloat(document.getElementById('registrationFee').value);
    
    if (!visitDate || !visitTime) {
        showMessage('Please fill in visit date and time', 'error');
        return;
    }
    
    if (isNaN(fee) || fee < 0) {
        showMessage('Please enter a valid registration fee', 'error');
        return;
    }
    
    const now = new Date();
    const registration = {
        patientId: patientId,
        patientName: patient.name,
        doctorId: doctorId,
        doctorName: doctor.name,
        department: doctor.department,
        registrationDate: now.toISOString().split('T')[0],
        registrationTime: now.toTimeString().split(' ')[0].substring(0, 5),
        visitDate: visitDate,
        visitTime: visitTime,
        status: 'Pending',
        fee: fee
    };
    
    try {
        const response = await fetch(`${API_BASE}/registrations`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registration)
        });
        
        const data = await response.json();
        
        if (response.ok) {
            showMessage(`Registration successful! Registration ID: ${data.id}, Fee: $${data.fee}`, 'success');
            document.getElementById('registrationForm').reset();
            loadRegistrations();
        } else {
            showMessage(data.error || 'Failed to create registration', 'error');
            console.error('Failed to create registration:', data);
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
        console.error('Network error:', error);
    }
}

async function loadRegistrations() {
    try {
        const response = await fetch(`${API_BASE}/registrations`);
        const registrations = await response.json();
        
        const listEl = document.getElementById('registrationList');
        if (registrations.length === 0) {
            listEl.innerHTML = '<p>No registration records available</p>';
            return;
        }
        
        listEl.innerHTML = registrations.map(registration => `
            <div class="item-card">
                <h4>Registration ${registration.id} <span class="status ${getStatusClass(registration.status)}">${registration.status}</span></h4>
                <p><strong>Patient:</strong> ${registration.patientName} (${registration.patientId})</p>
                <p><strong>Doctor:</strong> ${registration.doctorName} (${registration.doctorId})</p>
                <p><strong>Department:</strong> ${registration.department}</p>
                <p><strong>Registration Time:</strong> ${registration.registrationDate} ${registration.registrationTime}</p>
                <p><strong>Visit Time:</strong> ${registration.visitDate} ${registration.visitTime}</p>
                <p><strong>Registration Fee:</strong> $${registration.fee.toFixed(2)}</p>
            </div>
        `).join('');
    } catch (error) {
        showMessage('Failed to load registration records: ' + error.message, 'error');
    }
}

// Utility functions
function getStatusClass(status) {
    if (status === 'Pending') return 'pending';
    if (status === 'Completed' || status === 'Visited') return 'completed';
    if (status === 'Cancelled') return 'cancelled';
    return '';
}

// Set custom validation messages in English
function setupFormValidation() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        const inputs = form.querySelectorAll('input[required], select[required]');
        inputs.forEach(input => {
            // Set custom validation message
            input.addEventListener('invalid', function(e) {
                if (!e.target.validity.valid) {
                    if (e.target.type === 'email') {
                        e.target.setCustomValidity('Please enter a valid email address');
                    } else if (e.target.type === 'number') {
                        if (e.target.validity.valueMissing) {
                            e.target.setCustomValidity('Please fill in this field');
                        } else if (e.target.validity.rangeUnderflow) {
                            e.target.setCustomValidity('Value is too small');
                        } else if (e.target.validity.rangeOverflow) {
                            e.target.setCustomValidity('Value is too large');
                        } else {
                            e.target.setCustomValidity('Please enter a valid number');
                        }
                    } else if (e.target.type === 'date') {
                        e.target.setCustomValidity('Please select a date');
                    } else if (e.target.type === 'time') {
                        e.target.setCustomValidity('Please select a time');
                    } else {
                        e.target.setCustomValidity('Please fill in this field');
                    }
                }
            });
            
            // Clear custom message on input
            input.addEventListener('input', function(e) {
                e.target.setCustomValidity('');
            });
        });
        
        // Handle select elements
        const selects = form.querySelectorAll('select[required]');
        selects.forEach(select => {
            select.addEventListener('invalid', function(e) {
                if (!e.target.validity.valid) {
                    e.target.setCustomValidity('Please select an option');
                }
            });
            select.addEventListener('change', function(e) {
                e.target.setCustomValidity('');
            });
        });
    });
}

// Initialize on page load
window.addEventListener('load', () => {
    setupFormValidation();
    loadDoctors();
});


