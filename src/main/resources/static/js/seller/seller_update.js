document.addEventListener('DOMContentLoaded', () => {
    // 버튼 요소
    const editBtn = document.getElementById('edit-btn');
    const saveBtn = document.getElementById('save-btn');
    const cancelBtn = document.getElementById('cancel-btn');
    const leaveBtn = document.getElementById('leave-btn');

    // 폼 및 필드 요소
    const form = document.getElementById('seller-update-form');
    const editableFields = {
        shopName: document.getElementById('shopName'),
        name: document.getElementById('name'),
        shopAddress: document.getElementById('shopAddress'),
        tel: document.getElementById('tel')
    };

    // 수정 가능한 필드들을 readonly로 설정
    for (const key in editableFields) {
        if (editableFields[key]) {
            editableFields[key].readOnly = true;
        }
    }

    let initialFormState = {};

    // 수정 버튼 클릭 이벤트
    editBtn.addEventListener('click', () => {
        // 현재 폼 상태 저장
        for (const key in editableFields) {
            initialFormState[key] = editableFields[key].value;
        }

        // '수정 모드'로 전환
        for (const key in editableFields) {
            if (editableFields[key]) {
                editableFields[key].readOnly = false;
            }
        }

        // 버튼 상태 변경
        editBtn.style.display = 'none';
        leaveBtn.style.display = 'none';
        saveBtn.style.display = 'inline-block';
        cancelBtn.style.display = 'inline-block';
    });

    // 취소 버튼 클릭 이벤트
    cancelBtn.addEventListener('click', () => {
        window.location.reload();
    });

    // 폼 제출 전 유효성 검사
    if (form) {
        form.addEventListener('submit', (e) => {
            // 1. 변경 사항이 있는지 확인
            let isChanged = false;
            for (const key in editableFields) {
                if (editableFields[key] && initialFormState[key] !== editableFields[key].value) {
                    isChanged = true;
                    break;
                }
            }
            
            // 2. 현재 비밀번호가 입력되었는지 확인
            const currentPasswordInput = document.getElementById('currentPassword');
            if (!currentPasswordInput.value) {
                alert('정보를 수정하려면 현재 비밀번호를 입력해야 합니다.');
                e.preventDefault();
                return;
            }

            // 3. 변경 사항이 없으면 제출 막기
            if (!isChanged) {
                alert('변경된 내용이 없습니다.');
                e.preventDefault();
            }
        });
    }
}); 