const API_BASE = 'http://localhost:8080/api';

// 显示消息
function showMessage(text, type = 'info') {
    const messageEl = document.getElementById('message');
    messageEl.textContent = text;
    messageEl.className = `message ${type}`;
    messageEl.style.display = 'block';
    setTimeout(() => {
        messageEl.style.display = 'none';
    }, 3000);
}

// 切换标签页
function showTab(tabName, event) {
    // 隐藏所有标签内容
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    
    // 移除所有按钮的活动状态
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // 显示选中的标签内容
    document.getElementById(tabName).classList.add('active');
    
    // 激活对应的按钮
    if (event && event.target) {
        event.target.classList.add('active');
    } else {
        // 如果没有事件对象，通过按钮文本查找
        document.querySelectorAll('.tab-btn').forEach(btn => {
            if (btn.textContent.includes(tabName === 'patients' ? '患者' : 
                                          tabName === 'doctors' ? '医生' : 
                                          tabName === 'appointments' ? '预约' : '挂号')) {
                btn.classList.add('active');
            }
        });
    }
    
    // 加载对应标签的数据
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

// ============ 患者管理 ============
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
            showMessage(`患者注册成功！患者ID: ${data.id}`, 'success');
            document.getElementById('patientForm').reset();
            loadPatients();
        } else {
            showMessage(data.error || '注册失败', 'error');
        }
    } catch (error) {
        showMessage('网络错误: ' + error.message, 'error');
    }
}

async function loadPatients() {
    try {
        const response = await fetch(`${API_BASE}/patients`);
        const patients = await response.json();
        
        const listEl = document.getElementById('patientList');
        if (patients.length === 0) {
            listEl.innerHTML = '<p>暂无患者数据</p>';
            return;
        }
        
        listEl.innerHTML = patients.map(patient => `
            <div class="item-card">
                <h4>${patient.name} (ID: ${patient.id})</h4>
                <p><strong>手机号:</strong> ${patient.phone}</p>
                <p><strong>身份证:</strong> ${patient.idCard}</p>
                <p><strong>性别:</strong> ${patient.gender}</p>
                <p><strong>年龄:</strong> ${patient.age}</p>
            </div>
        `).join('');
    } catch (error) {
        showMessage('加载患者列表失败: ' + error.message, 'error');
    }
}

// ============ 医生管理 ============
async function loadDoctors() {
    try {
        const response = await fetch(`${API_BASE}/doctors`);
        const doctors = await response.json();
        
        const listEl = document.getElementById('doctorList');
        if (doctors.length === 0) {
            listEl.innerHTML = '<p>暂无医生数据</p>';
            return;
        }
        
        listEl.innerHTML = doctors.map(doctor => `
            <div class="doctor-card">
                <h4>${doctor.name} (${doctor.title})</h4>
                <p><strong>科室:</strong> ${doctor.department}</p>
                <p><strong>医生ID:</strong> ${doctor.id}</p>
                <p><strong>联系电话:</strong> ${doctor.phone}</p>
                <p><strong>工作时间:</strong> ${doctor.schedule}</p>
            </div>
        `).join('');
    } catch (error) {
        showMessage('加载医生列表失败: ' + error.message, 'error');
    }
}

// ============ 预约管理 ============
async function createAppointment(event) {
    event.preventDefault();
    
    const patientId = document.getElementById('appointmentPatientId').value;
    const doctorId = document.getElementById('appointmentDoctorId').value;
    
    // 获取患者和医生信息
    let patient, doctor;
    try {
        const patientRes = await fetch(`${API_BASE}/patients/${patientId}`);
        patient = await patientRes.json();
        
        const doctorRes = await fetch(`${API_BASE}/doctors/${doctorId}`);
        doctor = await doctorRes.json();
        
        if (!patientRes.ok || !doctorRes.ok) {
            showMessage('患者或医生不存在', 'error');
            return;
        }
    } catch (error) {
        showMessage('验证患者或医生信息失败: ' + error.message, 'error');
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
        status: '待就诊'
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
            showMessage(`预约创建成功！预约ID: ${data.id}`, 'success');
            document.getElementById('appointmentForm').reset();
            loadAppointments();
        } else {
            showMessage(data.error || '创建预约失败', 'error');
        }
    } catch (error) {
        showMessage('网络错误: ' + error.message, 'error');
    }
}

async function loadAppointments() {
    try {
        const response = await fetch(`${API_BASE}/appointments`);
        const appointments = await response.json();
        
        const listEl = document.getElementById('appointmentList');
        if (appointments.length === 0) {
            listEl.innerHTML = '<p>暂无预约数据</p>';
            return;
        }
        
        listEl.innerHTML = appointments.map(appointment => `
            <div class="item-card">
                <h4>预约 ${appointment.id} <span class="status ${getStatusClass(appointment.status)}">${appointment.status}</span></h4>
                <p><strong>患者:</strong> ${appointment.patientName} (${appointment.patientId})</p>
                <p><strong>医生:</strong> ${appointment.doctorName} (${appointment.doctorId})</p>
                <p><strong>科室:</strong> ${appointment.department}</p>
                <p><strong>预约日期:</strong> ${appointment.appointmentDate} ${appointment.appointmentTime}</p>
                ${appointment.status === '待就诊' ? `
                    <button class="btn btn-danger" onclick="cancelAppointment('${appointment.id}')">取消预约</button>
                ` : ''}
            </div>
        `).join('');
    } catch (error) {
        showMessage('加载预约列表失败: ' + error.message, 'error');
    }
}

async function cancelAppointment(id) {
    if (!confirm('确定要取消这个预约吗？')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/appointments/${id}`, {
            method: 'DELETE'
        });
        
        const data = await response.json();
        
        if (response.ok) {
            showMessage('预约已取消', 'success');
            loadAppointments();
        } else {
            showMessage(data.error || '取消预约失败', 'error');
        }
    } catch (error) {
        showMessage('网络错误: ' + error.message, 'error');
    }
}

// ============ 挂号管理 ============
async function createRegistration(event) {
    event.preventDefault();
    
    const patientId = document.getElementById('registrationPatientId').value.trim();
    const doctorId = document.getElementById('registrationDoctorId').value.trim();
    
    if (!patientId || !doctorId) {
        showMessage('请填写患者ID和医生ID', 'error');
        return;
    }
    
    // 获取患者和医生信息
    let patient, doctor;
    try {
        const patientRes = await fetch(`${API_BASE}/patients/${patientId}`);
        if (!patientRes.ok) {
            showMessage('患者不存在，请先注册患者', 'error');
            return;
        }
        patient = await patientRes.json();
        
        const doctorRes = await fetch(`${API_BASE}/doctors/${doctorId}`);
        if (!doctorRes.ok) {
            showMessage('医生不存在', 'error');
            return;
        }
        doctor = await doctorRes.json();
    } catch (error) {
        showMessage('验证患者或医生信息失败: ' + error.message, 'error');
        console.error('验证错误:', error);
        return;
    }
    
    const visitDate = document.getElementById('visitDate').value;
    const visitTime = document.getElementById('visitTime').value;
    const fee = parseFloat(document.getElementById('registrationFee').value);
    
    if (!visitDate || !visitTime) {
        showMessage('请填写就诊日期和时间', 'error');
        return;
    }
    
    if (isNaN(fee) || fee < 0) {
        showMessage('请输入有效的挂号费', 'error');
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
        status: '待就诊',
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
            showMessage(`挂号成功！挂号ID: ${data.id}，挂号费: ¥${data.fee}`, 'success');
            document.getElementById('registrationForm').reset();
            loadRegistrations();
        } else {
            showMessage(data.error || '创建挂号失败', 'error');
            console.error('创建挂号失败:', data);
        }
    } catch (error) {
        showMessage('网络错误: ' + error.message, 'error');
        console.error('网络错误:', error);
    }
}

async function loadRegistrations() {
    try {
        const response = await fetch(`${API_BASE}/registrations`);
        const registrations = await response.json();
        
        const listEl = document.getElementById('registrationList');
        if (registrations.length === 0) {
            listEl.innerHTML = '<p>暂无挂号记录</p>';
            return;
        }
        
        listEl.innerHTML = registrations.map(registration => `
            <div class="item-card">
                <h4>挂号 ${registration.id} <span class="status ${getStatusClass(registration.status)}">${registration.status}</span></h4>
                <p><strong>患者:</strong> ${registration.patientName} (${registration.patientId})</p>
                <p><strong>医生:</strong> ${registration.doctorName} (${registration.doctorId})</p>
                <p><strong>科室:</strong> ${registration.department}</p>
                <p><strong>挂号时间:</strong> ${registration.registrationDate} ${registration.registrationTime}</p>
                <p><strong>就诊时间:</strong> ${registration.visitDate} ${registration.visitTime}</p>
                <p><strong>挂号费:</strong> ¥${registration.fee.toFixed(2)}</p>
            </div>
        `).join('');
    } catch (error) {
        showMessage('加载挂号记录失败: ' + error.message, 'error');
    }
}

// 工具函数
function getStatusClass(status) {
    if (status === '待就诊') return 'pending';
    if (status === '已完成' || status === '已就诊') return 'completed';
    if (status === '已取消') return 'cancelled';
    return '';
}

// 页面加载时初始化
window.addEventListener('load', () => {
    loadDoctors();
});


