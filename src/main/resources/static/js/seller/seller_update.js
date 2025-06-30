document.addEventListener('DOMContentLoaded', async () => {
    const editBtn = document.getElementById('edit-btn');
    const saveBtn = document.getElementById('save-btn');
    const cancelBtn = document.getElementById('cancel-btn');
    const leaveBtn = document.getElementById('leave-btn');
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

    // 초기 판매자 정보 로드
    try {
        const response = await fetch('/api/sellers/info');
        const result = await response.json();

        if (result.success || result.code === "0") {
            const seller = result.data;
            document.getElementById('email').value = seller.email || '';
            document.getElementById('bizRegNo').value = seller.bizRegNo || '';
            editableFields.shopName.value = seller.shopName || '';
            editableFields.name.value = seller.name || '';
            editableFields.shopAddress.value = seller.shopAddress || '';
            editableFields.tel.value = seller.tel || '';
        } else {
            throw new Error(result.message || "판매자 정보를 불러올 수 없습니다.");
        }
    } catch (error) {
        console.error('판매자 정보 로드 오류:', error);
        alert('판매자 정보를 불러오는 중 오류가 발생했습니다.');
        window.location.href = '/seller/login';
    }

    // 수정 버튼 클릭 이벤트
    editBtn.addEventListener('click', () => {
        // 현재 폼 상태 저장
        for (const key in editableFields) {
            if (editableFields[key]) {
                initialFormState[key] = editableFields[key].value;
            }
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

    // 폼 제출 처리
    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

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
                return;
            }

            // 3. 변경 사항이 없으면 제출 막기
            if (!isChanged) {
                alert('변경된 내용이 없습니다.');
                return;
            }

            // 4. 서버에 수정 요청
            try {
                const updateData = {
                    shopName: editableFields.shopName.value.trim(),
                    name: editableFields.name.value.trim(),
                    shopAddress: editableFields.shopAddress.value.trim(),
                    tel: editableFields.tel.value.trim(),
                    currentPassword: currentPasswordInput.value
                };

                const response = await fetch('/api/sellers/info', {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(updateData)
                });

                const result = await response.json();

                if (result.rtcd === "0") {
                    alert('판매자 정보가 수정되었습니다.');
                    window.location.reload();
                } else {
                    throw new Error(result.rtmsg || "정보 수정에 실패했습니다.");
                }
            } catch (error) {
                console.error('정보 수정 오류:', error);
                alert('정보 수정 실패: ' + error.message);
            }
        });
    }
});
