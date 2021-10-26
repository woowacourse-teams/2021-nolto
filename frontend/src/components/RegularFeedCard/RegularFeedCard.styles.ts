import { hoverZoomImg } from 'commonStyles';
import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  position: relative;
  flex-direction: column;
  min-width: 9rem;
  width: 100%;
  gap: 0.5rem;
  opacity: 0.99;

  & img,
  video {
    transition: all 0.2s ease;

    &:hover {
      transform: scale(1.05);
    }
  }

  & > .sos {
    position: absolute;
    left: -0.5rem;
    top: 2.5rem;
  }

  & > .link {
    &:hover {
      text-decoration: underline;

      ${hoverZoomImg}
    }
  }
`;

const RegularCardImgWrapper = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 7px;
  padding-top: 100%;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.1);

  & > img,
  video {
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
  margin-top: 0.5rem;
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

export default { Root, RegularCardImgWrapper, Content };
