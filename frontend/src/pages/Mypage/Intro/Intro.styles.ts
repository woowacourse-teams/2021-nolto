import styled, { css } from 'styled-components';

import Thumbnail from 'components/Thumbnail/Thumbnail';
import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { defaultShadow, hoverLayer } from 'commonStyles';

const Root = styled.form`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 3rem 2rem;
  gap: 3rem;
  max-width: 12rem;
  width: 100%;
  height: fit-content;
  border-radius: 0.75rem;
  ${defaultShadow};

  @media ${MEDIA_QUERY.DESKTOP_SMALL} {
    display: flex;
    flex-direction: row;
    max-width: 36rem;
    padding: 2rem;
  }

  @media ${MEDIA_QUERY.TABLET} {
    padding: 1rem;
    gap: 1.5rem;
  }
`;

const ImageWrapper = styled.div`
  position: relative;
`;

const userImage = css`
  width: 6rem;
  height: 6rem;
  object-fit: cover;
  border-radius: 50%;

  @media ${MEDIA_QUERY.TABLET} {
    width: 5.25rem;
    height: 5.25rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 4.5rem;
    height: 4.5rem;
  }
`;

const UserImage = styled(Thumbnail)`
  ${userImage};
`;

const CameraLabel = styled.label`
  position: absolute;
  left: 0.35rem;
  bottom: 0.75rem;
  background: ${PALETTE.WHITE_400};
  width: 1.25rem;
  height: 1.25rem;
  border: 1px solid ${PALETTE.BLACK_200};
  border-radius: 50%;

  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;

  & > input {
    position: absolute;
    opacity: 0;
    width: 0;
    height: 0;
  }
`;

const Content = styled.div`
  width: 100%;
`;

const TopContainer = styled.div`
  display: flex;
  gap: 1rem;
`;

const nameStyle = css`
  font-size: 18px;
  line-height: 18px;
  font-weight: 700;
  width: 100%;
`;

const Name = styled.div`
  ${nameStyle};
`;

const NameInput = styled.input`
  ${nameStyle};
  border: none;
  border-bottom: 1px solid ${PALETTE.BLACK_100};

  &:focus {
    outline: none;
  }
`;

const editButtonStyle = css`
  background: transparent;
  border: none;
  flex-shrink: 0;
  color: ${PALETTE.GRAY_500};

  ${hoverLayer({ borderRadius: '25%' })};
`;

const EditButton = styled.button`
  ${editButtonStyle};
`;

const EditDoneButton = styled.button`
  ${editButtonStyle};
`;

const ValidationMessage = styled.span<{ isValid: boolean }>`
  font-size: 0.5rem;
  color: ${({ isValid }) => (isValid ? PALETTE.ALERT_GREEN : PALETTE.ALERT_RED)};
`;

const DetailContainer = styled.div`
  margin-top: 1rem;
`;

const detailStyle = css`
  font-size: 0.85rem;
  color: ${PALETTE.BLACK_200};
  padding-bottom: 0.25rem;
`;

const DetailText = styled.div`
  ${detailStyle};
`;

const BioInput = styled.input`
  ${detailStyle};
  border: none;
  border-bottom: 1px solid ${PALETTE.BLACK_200};

  &:focus {
    outline: none;
  }
`;

export default {
  Root,
  ImageWrapper,
  UserImage,
  CameraLabel,
  Content,
  TopContainer,
  Name,
  NameInput,
  EditButton,
  EditDoneButton,
  ValidationMessage,
  DetailContainer,
  DetailText,
  BioInput,
};
