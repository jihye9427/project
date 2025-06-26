// ✅ 판매자 로그인 폼 제출 처리
document.querySelector('.seller-form').addEventListener('submit', async function(e) {
  e.preventDefault();

  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  try {
    const formData = new URLSearchParams();
    formData.append('email', email);
    formData.append('password', password);

    const response = await fetch('/seller/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: formData,
    });

    if (response.ok && response.redirected) {
      window.location.href = response.url;
    } else {
      alert("이메일 또는 비밀번호가 일치하지 않습니다.");
    }
  } catch (error) {
    console.error('Login Error:', error);
    alert('로그인 처리 중 오류가 발생했습니다.');
  }
});