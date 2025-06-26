// ✅ 로그아웃 함수: 어디서든 호출 가능
function logout() {
  // 🧹 1. 저장된 로그인 상태 초기화
  localStorage.removeItem("userType");
  localStorage.removeItem("userEmail");
  localStorage.removeItem("userName");

  // ✅ 전체 삭제하고 싶을 경우
  // localStorage.clear();

  // 📢 2. 사용자에게 메시지 출력
  alert("로그아웃 되었습니다.");

  // 🏠 3. 메인페이지로 이동
  window.location.href = "index.html";
}

document.getElementById('logout-btn').addEventListener('click', async () => {
  const isBuyer = window.location.pathname.startsWith('/buyer/');
  const isSeller = window.location.pathname.startsWith('/seller/');

  let logoutUrl;
  if (isBuyer) {
    logoutUrl = '/buyer/logout';
  } else if (isSeller) {
    logoutUrl = '/seller/logout';
  } else {
    // 혹시 모를 예외상황 (예: 홈화면에서 로그아웃) - 세션에 따라 결정해야하지만, 우선순위를 정합니다.
    // 여기서는 간단히 buyer를 우선으로 두거나, 공통 로그아웃 URL을 가정할 수 있습니다.
    // 지금은 buyer/seller 페이지에만 로그아웃 버튼이 있다고 가정합니다.
    return;
  }

  if (!confirm('로그아웃 하시겠습니까?')) {
    return;
  }

  try {
    const response = await fetch(logoutUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
    });

    const result = await response.json();

    if (response.ok && result.success) {
      alert('로그아웃 되었습니다.');
      window.location.href = '/'; // 홈으로 이동
    } else {
      alert('로그아웃 실패: ' + result.message);
    }
  } catch (error) {
    console.error('Logout Error:', error);
    alert('로그아웃 중 오류가 발생했습니다.');
  }
});
