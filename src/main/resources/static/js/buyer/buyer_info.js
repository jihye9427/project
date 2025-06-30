// API 호출해서 사용자 정보 가져오기
window.onload = () => {
  fetch('/api/account/info')
    .then(res => res.json())
    .then(data => {
      document.getElementById('email').textContent = data.email;
      document.getElementById('name').textContent = data.name;
      document.getElementById('phone').textContent = data.phone;
      document.getElementById('joinDate').textContent = data.joinDate;
    })
    .catch(err => {
      alert("계정 정보를 불러오는 데 실패했습니다.");
      console.error(err);
    });
}

function confirmDelete() {
  if (confirm("정말로 탈퇴하시겠습니까?")) {
    fetch('/api/account/delete', { method: 'DELETE' })
      .then(res => {
        if (res.ok) {
          alert('탈퇴가 완료되었습니다.');
          window.location.href = '/';
        } else {
          alert('탈퇴에 실패했습니다.');
        }
      })
      .catch(err => {
        alert('오류가 발생했습니다.');
        console.error(err);
      });
  }
}

function updateInfo() {
  // 회원정보 수정 로직을 구현해야 합니다.
  // 이 함수는 회원정보 수정 후 호출될 예정입니다.
  // 현재는 빈 함수로 남겨두었습니다.
}

function updateInfoSubmit(event) {
  event.preventDefault();
  updateInfo();
}

function updateInfoForm() {
  // 회원정보 수정 폼을 표시하는 로직을 구현해야 합니다.
  // 이 함수는 회원정보 수정 폼을 표시할 때 호출될 예정입니다.
  // 현재는 빈 함수로 남겨두었습니다.
}

function updateInfoFormSubmit(event) {
  event.preventDefault();
  updateInfoForm();
}

function updateInfoFormReset() {
  // 회원정보 수정 폼을 초기화하는 로직을 구현해야 합니다.
  // 이 함수는 회원정보 수정 폼을 초기화할 때 호출될 예정입니다.
  // 현재는 빈 함수로 남겨두었습니다.
}

function updateInfoFormResetSubmit() {
  updateInfoFormReset();
}