import React from 'react';
import { createMemoryHistory } from 'history';

import { customRender, fireEvent, waitFor } from 'test-util';
import { UPLOAD_VALIDATION_MSG } from 'constants/message';
import ROUTE from 'constants/routes';
import FeedUploadForm from './FeedUploadForm';
import { MOCK_FEED_TO_UPLOAD } from '__mocks__/fixture/Feeds';

const submitMock = jest.fn();

describe('FeedUploadForm 테스트', () => {
  it('필수 input에 값을 입력하지 않고 제출을 누르면 경고창을 표시한다.', async () => {
    const { getByRole, getByText } = customRender(<FeedUploadForm onFeedSubmit={submitMock} />);

    const submitButton = await waitFor(() =>
      getByRole('button', {
        name: /등록/i,
      }),
    );

    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(getByText(UPLOAD_VALIDATION_MSG.TITLE_REQUIRED)).toBeInTheDocument();
      expect(getByText(UPLOAD_VALIDATION_MSG.CONTENT_REQUIRED)).toBeInTheDocument();
      expect(getByText(UPLOAD_VALIDATION_MSG.STEP_REQUIRED)).toBeInTheDocument();
    });
  });

  it('전시 중 checkbox를 클릭하면 배포 URL input을 보여준다', async () => {
    const { getByLabelText, getByText } = customRender(
      <FeedUploadForm onFeedSubmit={submitMock} />,
    );

    const completeCheckbox = getByLabelText(/전시중/i);
    fireEvent.click(completeCheckbox);

    await waitFor(() => {
      expect(getByText(/배포 URL/i)).toBeInTheDocument();
    });
  });

  it('모든 input을 입력하고 등록을 누르면 메인 페이지로 이동한다.', async () => {
    const history = createMemoryHistory();

    const { getByLabelText, getByRole } = customRender(
      <FeedUploadForm onFeedSubmit={submitMock} />,
    );

    const titleInput = getByLabelText(/제목/i);
    const contentInput = getByLabelText(/내용/i);
    const stepInput = getByLabelText(/조립중/i);
    const sosInput = getByLabelText(/SOS/i);

    fireEvent.change(titleInput, { target: { value: '테스트 제목' } });
    fireEvent.change(contentInput, { target: { value: '테스트 내용입니다.' } });
    fireEvent.click(stepInput);
    fireEvent.click(sosInput);

    const submitButton = getByRole('button', {
      name: /등록/i,
    });

    fireEvent.click(submitButton);

    customRender(<FeedUploadForm onFeedSubmit={submitMock} />);

    await waitFor(() => {
      expect(history.location.pathname).toBe(ROUTE.HOME);
    });
  });

  it('피드 수정 시 수정 버튼을 보여준다.', async () => {
    const { getByRole } = customRender(
      <FeedUploadForm onFeedSubmit={submitMock} initialFormValue={MOCK_FEED_TO_UPLOAD} />,
    );

    await waitFor(() => {
      expect(getByRole('button', { name: /수정/i })).toBeInTheDocument();
    });
  });

  it('피드 수정 시 필수 입력 input의 값들이 채워져 있다.', async () => {
    const { getByLabelText } = customRender(
      <FeedUploadForm onFeedSubmit={submitMock} initialFormValue={MOCK_FEED_TO_UPLOAD} />,
    );

    const titleInput = getByLabelText(/제목/i);
    const contentInput = getByLabelText(/내용/i);

    expect((titleInput as HTMLInputElement).value).toBe(MOCK_FEED_TO_UPLOAD.title);
    expect((contentInput as HTMLInputElement).value).toBe(MOCK_FEED_TO_UPLOAD.content);
  });
});
