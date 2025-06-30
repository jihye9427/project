// 회원가입 처리 (기존 URL 유지)
async function signup() {
    const response = await fetch('/api/sellers/join', {  // join 유지
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            bizRegNo: document.getElementById('bizRegNo').value,
            shopName: document.getElementById('shopName').value,
            name: document.getElementById('name').value,
            shopAddress: document.getElementById('shopAddress').value,
            tel: document.getElementById('tel').value,
            birth: document.getElementById('birth').value
        })
    });

    const result = await response.json();
    if (result.code === '0') {
        alert('회원가입 성공!');
        window.location.href = '/seller/login';
    } else {
        alert(result.message);
    }
}

// 로그인 처리
async function login() {
    const response = await fetch('/api/sellers/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        })
    });

    const result = await response.json();
    if (result.code === '0') {
        alert('로그인 성공!');
        window.location.href = '/seller/mypage';
    } else {
        alert(result.message);
    }
}

// 로그아웃 처리
async function logout() {
    const response = await fetch('/api/sellers/logout', {
        method: 'POST'
    });

    const result = await response.json();
    alert(result.message);
    window.location.href = '/seller/login';
}

// 회원정보 조회
async function loadMemberInfo() {
    const response = await fetch('/api/sellers/info');
    const result = await response.json();

    if (result.code === '0') {
        document.getElementById('email').value = result.data.email;
        document.getElementById('bizRegNo').value = result.data.bizRegNo;
        document.getElementById('shopName').value = result.data.shopName;
        document.getElementById('name').value = result.data.name;
        document.getElementById('shopAddress').value = result.data.shopAddress;
        document.getElementById('tel').value = result.data.tel;
        if (result.data.birth) {
            document.getElementById('birth').value = result.data.birth;
        }
    } else {
        alert(result.message);
        window.location.href = '/seller/login';
    }
}

// 회원정보 수정
async function updateMemberInfo() {
    const response = await fetch('/api/sellers/info', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            currentPassword: document.getElementById('currentPassword').value,
            shopName: document.getElementById('shopName').value,
            name: document.getElementById('name').value,
            shopAddress: document.getElementById('shopAddress').value,
            tel: document.getElementById('tel').value
        })
    });

    const result = await response.json();
    alert(result.message);
    if (result.code === '0') {
        window.location.reload();
    }
}

// 회원 탈퇴
async function leaveMember() {
    if (!confirm('정말 탈퇴하시겠습니까?')) return;

    const response = await fetch('/api/sellers/leave', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            password: document.getElementById('password').value
        })
    });

    const result = await response.json();
    alert(result.message);
    if (result.code === '0') {
        window.location.href = '/';
    }
}

// 이메일 중복 체크
async function checkEmail() {
    const email = document.getElementById('email').value;
    if (!email) return;

    const response = await fetch(`/api/sellers/check-email?email=${email}`);
    const result = await response.json();

    const messageElement = document.getElementById('emailMessage');
    messageElement.textContent = result.message;
    messageElement.className = result.code === '0' ? 'success' : 'error';
}