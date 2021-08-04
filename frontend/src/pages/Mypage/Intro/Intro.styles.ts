import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.div`
  display: flex;
  align-items: center;
  padding: 1rem 2rem;
  gap: 2rem;
  width: 32rem;
  height: 10rem;
  border-radius: 0.5rem;
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
`;

const Name = styled.div`
  font-weight: 700;
  width: 100%;
`;

const EditButton = styled.button`
  background: transparent;
  border: none;
  flex: 1;
`;

const ValidationMessage = styled.span<{ isValid: boolean }>`
  font-size: 0.5rem;
  color: ${({ isValid }) => (isValid ? PALETTE.ALERT_GREEN : PALETTE.ALERT_RED)};
`;

const DetailContainer = styled.div`
  margin-top: 1rem;
`;

const DetailText = styled.div`
  font-size: 0.75rem;
  color: ${PALETTE.BLACK_200};
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
