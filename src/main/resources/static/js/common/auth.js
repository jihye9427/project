// 페이지 로드 시 로그인 상태 확인
document.addEventListener("DOMContentLoaded", function() {
    checkLoginStatus();
});

/**
 * 서버에 로그인 상태를 확인하고 UI를 업데이트하는 함수
 */
function checkLoginStatus() {
    fetch("/api/auth/status", {
        method: "GET",
        credentials: "include" // 세션 쿠키를 주고받기 위해 필수
    })
    .then(res => {
        if (!res.ok) throw new Error('서버 응답 오류');
        return res.json();
    })
    .then(data => {
        if (data.rtcd === "0" && data.data) {
            updateUIForLoggedInUser(data.data.user, data.data.userType);
        } else {
            updateUIForGuestUser();
        }
    })
    .catch(err => {
        console.error("로그인 상태 확인 실패:", err);
        updateUIForGuestUser();
    });
}

/**
 * 로그인된 사용자를 위한 UI 변경
 * @param {object} user - 사용자 정보 객체
 * @param {string} userType - 사용자 유형 ('buyer' 또는 'seller')
 */
function updateUIForLoggedInUser(user, userType) {
    const navButtons = document.querySelector('.navbar8-buttons1');
    if (navButtons) {
        const myPageUrl = userType === 'buyer' ? '/buyer/mypage' : '/seller/mypage';
        const userName = user.name || '사용자'; // 이름 필드가 없을 경우 대비

        navButtons.innerHTML = `
            <div class="user-info">
                <span style="margin-right: 16px;">${userName}님, 환영합니다.</span>
                <button onclick="location.href='${myPageUrl}'" class="thq-button-outline">마이페이지</button>
                <button onclick="logout()" class="thq-button-filled">로그아웃</button>
            </div>
        `;
    }
}

/**
 * 비로그인 상태일 때의 UI 변경
 */
function updateUIForGuestUser() {
    const navButtons = document.querySelector('.navbar8-buttons1');
    if (navButtons) {
        navButtons.innerHTML = `
            <button class="thq-button-outline" onclick="location.href='/login'">로그인</button>
            <button class="thq-button-filled" onclick="location.href='/signup'">회원가입</button>
        `;
    }
}

/**
 * 로그아웃 처리
 */
function logout() {
    fetch("/api/auth/logout", {
        method: "POST",
        credentials: "include"
    })
    .then(res => res.json())
    .then(data => {
        if (data.rtcd === "0") {
            alert("로그아웃되었습니다.");
            window.location.href = "/";
        } else {
            alert("로그아웃 실패: " + (data.rtmsg || "오류가 발생했습니다."));
        }
    })
    .catch(err => {
        console.error("로그아웃 처리 중 오류:", err);
        alert("로그아웃 중 오류가 발생했습니다.");
    });
} 