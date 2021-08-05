import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.div`
  display: flex;
  align-items: center;
  padding: 1rem 2rem;
  gap: 2rem;
  width: 32rem;
  height: 10rem;
  border-radius: 0.75rem;
  box-shadow: 4px 4px 8px 4px rgba(85, 85, 85, 0.2);
`;

const ImageWrapper = styled.form`
  position: relative;
`;

const UserImage = styled.img`
  width: 6rem;
  border-radius: 50%;
`;

const CameraLabel = styled.label`
  position: absolute;
  left: 0.75rem;
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

const Name = styled.div<{ isEditing: boolean }>`
  font-size: 18px;
  font-weight: 700;
  width: 100%;
  padding-bottom: 0.25rem;
  border-bottom: ${({ isEditing }) => isEditing && `1px solid ${PALETTE.BLACK_100}`};

  &:focus {
    outline: none;
  }
`;

const EditButton = styled.button`
  background: transparent;
  border: none;
  flex-shrink: 0;
  color: ${PALETTE.GRAY_500};
  position: relative;

  &:hover::after {
    content: '';
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0.1;
    background-color: ${PALETTE.BLACK_400};
  }
`;

const ValidationMessage = styled.span<{ isValid: boolean }>`
  font-size: 0.5rem;
  color: ${({ isValid }) => (isValid ? PALETTE.ALERT_GREEN : PALETTE.ALERT_RED)};
`;

const DetailContainer = styled.div`
  margin-top: 1rem;
`;

const DetailText = styled.div<{ isEditing?: boolean }>`
  font-size: 0.75rem;
  color: ${PALETTE.BLACK_200};
  padding-bottom: 0.25rem;
  border-bottom: ${({ isEditing }) => isEditing && `1px solid ${PALETTE.BLACK_100}`};

  &:focus {
    outline: none;
    border-bottom: 1px solid ${PALETTE.BLACK_200};
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
  EditButton,
  ValidationMessage,
  DetailContainer,
  DetailText,
};
