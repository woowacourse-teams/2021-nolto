import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  min-width: 12.5rem;
  width: 12.5rem;
  gap: 0.5rem;
`;

export const RegularCardImgWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 7px;
  height: 12.5rem;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.1);

  & > img {
    height: 100%;
  }
`;

const Content = styled.div`
  display: flex;
  flex-direction: column;
  height: 6rem;
  padding: 0.25rem;
  gap: 0.25rem;

  & > h3 {
    font-size: 16px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  & > p {
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    font-size: 14px;
  }
`;

export default { Root, Content };
