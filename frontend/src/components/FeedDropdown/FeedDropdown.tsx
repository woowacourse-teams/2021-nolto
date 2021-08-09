import React from 'react';
import { useHistory } from 'react-router-dom';

import Dropdown from 'components/@common/Dropdown/Dropdown';
import useDialog from 'context/dialog/useDialog';
import useSnackBar from 'context/snackBar/useSnackBar';
import useFeedDelete from 'hooks/mutations/useFeedDelete';
import ROUTE from 'constants/routes';
import { FeedDetail } from 'types';
import Styled from './FeedDropdown.styles';

interface Props {
  feedDetail: FeedDetail;
}

const FeedDropdown = ({ feedDetail }: Props) => {
  const dialog = useDialog();
  const snackBar = useSnackBar();
  const deleteMutation = useFeedDelete();
  const history = useHistory();

  const handleModify = () => {
    history.push(ROUTE.MODIFY, { feedDetail });
  };

  const handleDelete = () => {
    dialog.confirm('정말로 삭제하시겠습니까?', () => {
      deleteMutation.mutate(
        { feedId: feedDetail.id },
        {
          onSuccess: () => {
            snackBar.addSnackBar('success', '토이 프로젝트가 삭제되었습니다.');
            history.push(ROUTE.HOME);
          },
          onError: (error) => {
            snackBar.addSnackBar('error', error.message);
          },
        },
      );
    });
  };

  return (
    <Styled.Root>
      <Dropdown>
        <button onClick={handleModify}>수정</button>
        <button onClick={handleDelete} className="delete-button">
          삭제
        </button>
      </Dropdown>
    </Styled.Root>
  );
};

export default FeedDropdown;
