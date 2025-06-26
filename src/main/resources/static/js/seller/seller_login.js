// ✅ 판매자 로그인 폼 제출 처리
document.querySelector('.seller-form').addEventListener('submit', async function(e) {
  e.preventDefault();

  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  try {
    const response = await fetch('/seller/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify({ email, password }),
    });

    if (response.ok) {
      const result = await response.json();
      if (result.success) {
        alert('로그인 성공! 메인 페이지로 이동합니다.');
        window.location.href = '/';
      } else {
        alert('로그인 실패: ' + result.message);
      }
    } else {
      const result = await response.json();
      alert('로그인 실패: ' + result.message);
    }
  } catch (error) {
    console.error('Login Error:', error);
    alert('로그인 처리 중 오류가 발생했습니다.');
  }
});