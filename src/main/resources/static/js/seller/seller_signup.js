// ✅ DOM 로딩 시 각 입력 필드에 이벤트 바인딩
document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("email").addEventListener("input", validateEmail);
  document.getElementById("password").addEventListener("input", validatePassword);
  document.getElementById("password2").addEventListener("input", validatePasswordMatch);
  document.getElementById("bizRegNo").addEventListener("input", function () {
    this.value = formatBizNo(this.value);
    validateBizReg();
  });
  document.getElementById("shopName").addEventListener("input", validateShopName);
  document.getElementById("name").addEventListener("input", validateName);
  document.getElementById("shopAddress").addEventListener("input", validateShopAddress);
  document.getElementById("tel").addEventListener("input", function () {
    this.value = formatPhoneNumber(this.value);
    validateTel();
  });
  document.getElementById("birth").addEventListener("change", validateBirth);
});


// -------------------------------------------
// 🔢 자동 포맷 함수들
// -------------------------------------------

// ✅ 사업자등록번호 하이픈 자동 삽입 (000-00-00000)
function formatBizNo(value) {
  const numbers = value.replace(/\D/g, '').slice(0, 10);
  if (numbers.length < 4) return numbers;
  if (numbers.length < 6) return numbers.slice(0, 3) + '-' + numbers.slice(3);
  return numbers.slice(0, 3) + '-' + numbers.slice(3, 5) + '-' + numbers.slice(5);
}

// ✅ 전화번호 하이픈 자동 삽입 (010-1234-5678)
function formatPhoneNumber(value) {
  const numbers = value.replace(/\D/g, '').slice(0, 11);
  if (numbers.startsWith("02")) {
    if (numbers.length < 3) return numbers;
    if (numbers.length < 6) return numbers.slice(0, 2) + '-' + numbers.slice(2);
    return numbers.slice(0, 2) + '-' + numbers.slice(2, 6) + '-' + numbers.slice(6);
  } else {
    if (numbers.length < 4) return numbers;
    if (numbers.length < 8) return numbers.slice(0, 3) + '-' + numbers.slice(3);
    return numbers.slice(0, 3) + '-' + numbers.slice(3, 7) + '-' + numbers.slice(7);
  }
}


// -------------------------------------------
// ✅ 유효성 검사 함수들
// -------------------------------------------
//이메일
function validateEmail() {
  const val = document.getElementById("email").value.trim();
  const error = document.getElementById("error-email");
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  error.textContent = (!regex.test(val) || val.length > 50)
    ? "올바른 이메일 형식이여야 합니다." : "";
}
// 비밀번호
function validatePassword() {
  const val = document.getElementById("password").value;
  const error = document.getElementById("error-password");
  const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,15}$/;
  error.textContent = !regex.test(val)
    ? "8~15자, 영문+숫자+특수문자를 포함해야 합니다." : "";
  validatePasswordMatch();
}
// 비밀번호 확인
function validatePasswordMatch() {
  const pw = document.getElementById("password").value;
  const pw2 = document.getElementById("password2").value;
  const error = document.getElementById("error-password2");
  error.textContent = (pw !== pw2) ? "비밀번호가 일치하지 않습니다." : "";
}
// 사업자등록번호
function validateBizReg() {
  const val = document.getElementById("bizRegNo").value.trim();
  const error = document.getElementById("error-bizRegNo");
  const regex = /^\d{3}-\d{2}-\d{5}$/;
  error.textContent = !regex.test(val)
    ? "사업자등록번호를 입력해주세요." : "";
}
// 상호명
function validateShopName() {
  const val = document.getElementById("shopName").value.trim();
  const error = document.getElementById("error-shopName");
  error.textContent = (val.length < 2 || val.length > 30) // max 24 -> 30
    ? "상호명을 입력해주세요." : "";
}
// 이름
function validateName() {
  const val = document.getElementById("name").value.trim();
  const error = document.getElementById("error-name");
  const isKor = /^[가-힣]{2,8}$/.test(val);
  const isEng = /^[a-zA-Z\s]{2,45}$/.test(val);
  error.textContent = (!isKor && !isEng)
    ? "이름은 한글 2~8자 또는 영문 2~45자로 입력해주세요." : "";
}
// 가게 주소
function validateShopAddress() {
  const val = document.getElementById("shopAddress").value.trim();
  const error = document.getElementById("error-shopAddress");
  error.textContent = (val.length < 5 || val.length > 200)
    ? "가게 주소를 입력해주세요." : "";
}
// 전화번호
function validateTel() {
  const val = document.getElementById("tel").value.trim();
  const error = document.getElementById("error-tel");
  const regex = /^(01[016789]|02|0[3-9][0-9])-\d{3,4}-\d{4}$/;
  error.textContent = !regex.test(val)
    ? "전화번호를 입력해주세요." : "";
}
// 생년월일
function validateBirth() {
  const val = document.getElementById("birth").value;
  const error = document.getElementById("error-birth");
  error.textContent = !val ? "생년월일을 입력해주세요." : "";
}


// -------------------------------------------
// ✅ 폼 제출 이벤트 처리 (제거)
// -------------------------------------------
// 아래 로직은 현재 HTML 구조와 맞지 않으므로 제거합니다.
// 폼 제출은 서버사이드에서 처리하고, 클라이언트에서는 실시간 유효성 검사만 담당합니다.
