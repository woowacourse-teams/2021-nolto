import styled, { css } from 'styled-components';

import { PALETTE } from 'constants/palette';
import { hoverLayer } from 'commonStyles';

const Root = styled.form`
  display: flex;
  align-items: center;
  padding: 1rem 2rem;
  gap: 2rem;
  width: 36rem;
  height: 10rem;
  border-radius: 0.75rem;
  box-shadow: 4px 4px 8px 4px rgba(85, 85, 85, 0.2);
`;

const ImageWrapper = styled.div`
  position: relative;
`;

const UserImage = styled.img`
  width: 6rem;
  height: 6rem;
  object-fit: cover;
  border-radius: 50%;
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
  flex-grow: 1;
`;

const TopContainer = styled.div`
  display: flex;
  gap: 1rem;
`;

const nameStyle = css`
  font-size: 18px;
  font-weight: 700;
  width: 100%;
  padding-bottom: 0.25rem;
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

  ${hoverLayer({})};
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
