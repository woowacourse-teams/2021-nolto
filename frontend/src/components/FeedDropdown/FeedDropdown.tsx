import React from 'react';

import Dropdown from 'components/@common/Dropdown/Dropdown';
import Styled from './FeedDropdown.styles';
import useNotification from 'context/notification/useNotification';
import useFeedDelete from 'hooks/queries/useFeedDelete';
import useSnackBar from 'context/snackBar/useSnackBar';
import { useHistory } from 'react-router-dom';
import ROUTE from 'constants/routes';

interface Props {
  feedId: number;
}

const FeedDropdown = ({ feedId }: Props) => {
  const notification = useNotification();
  const snackBar = useSnackBar();
  const deleteMutation = useFeedDelete();
  const history = useHistory();

  const handleModify = () => {
    history.push(ROUTE.MODIFY, { feedId });
  };

  const handleDelete = () => {
    notification.confirm('정말로 삭제하시겠어요?', () => {
      deleteMutation.mutate(
        { feedId },
        {
          onSuccess: () => {
            snackBar.addSnackBar('success', '토이 프로젝트가 삭제되었어요');
            history.push(ROUTE.HOME);
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
