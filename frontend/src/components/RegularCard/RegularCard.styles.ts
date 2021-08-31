import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  min-width: 12.5rem;
  width: 100%;
  gap: 0.5rem;

  & .project-image {
    transition: all 0.2s ease;

    &:hover {
      transform: scale(1.05);
    }
  }
`;

export const RegularCardImgWrapper = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 7px;
  padding-top: 100%;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.1);

  & > img {
    position: absolute;
    left: 0;
    top: 0;
    height: 100%;
    width: 100%;
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
