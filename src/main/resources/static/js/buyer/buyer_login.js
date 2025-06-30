// 회원가입 처리 (기존 URL 유지)
async function signup() {
    const response = await fetch('/api/buyers/join', {  // join 유지
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            name: document.getElementById('name').value,
            nickname: document.getElementById('nickname').value,
            tel: document.getElementById('tel').value
        })
    });
    
    const result = await response.json();
    if (result.code === '0') {
        alert('회원가입 성공!');
        window.location.href = '/buyer/login';
    } else {
        alert(result.message);
    }
}

// 로그인 처리
async function login() {
    const response = await fetch('/api/buyers/login', {
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
        window.location.href = '/buyer/mypage';
    } else {
        alert(result.message);
    }
}