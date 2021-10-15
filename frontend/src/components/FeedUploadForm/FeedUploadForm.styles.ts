import styled, { keyframes } from 'styled-components';

import TextArea from 'components/@common/TextArea/TextArea';
import TextButton from 'components/@common/TextButton/TextButton';
import Tooltip from 'components/@common/Tooltip/Tooltip';
import { FONT_SIZE } from 'constants/styles';
import ToyboxIcon from 'assets/toybox.svg';
import { defaultShadow } from 'commonStyles';

export const Form = styled.form`
  margin-bottom: 5rem;
`;

const FormContainer = styled.div`
  padding: 4rem 3rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  border-radius: 1.5rem;
  margin-bottom: 2.5rem;
  ${defaultShadow};
`;

const MarkdownContainer = styled.div`
  display: flex;
  gap: 0.5rem;

  & > * {
    flex-shrink: 0;
    flex-basis: 50%;
  }
`;

const VerticalWrapper = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  border-bottom: 2rem;
  width: 100%;

  & input,
  textarea {
    box-shadow: 2px 2px 2px 0 rgba(253, 161, 133, 0.5);
  }
`;

export const ContentTextArea = styled(TextArea)`
  width: 50%;
  height: 31.25rem;
  border-radius: 0.5rem;
`;

const MarkdownWrapper = styled.div`
  width: 50%;
  height: 31.25rem;
  border-radius: 0.5rem;
  padding: 0.5rem 1rem;
  overflow-y: auto;

  ${defaultShadow};
`;

const InputsContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
`;

const StretchWrapper = styled.div`
  display: flex;
  align-items: center;
  border-bottom: 2rem;
  width: 100%;

  & > .stretch-label {
    min-width: 7rem;
  }

  & .input-box {
    width: 100%;
  }

  & input {
    box-shadow: 2px 2px 2px 0 rgba(253, 161, 133, 0.5);
  }
`;

const LevelWrapper = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  min-width: 7rem;
`;

const SOSLabel = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-left: auto;
`;

export const StyledButton = styled(TextButton.Regular)`
  width: 12rem;
  height: 2.25rem;
  font-size: 1rem;
  border-radius: 7px;
  margin-top: 1rem;
`;

const ButtonsWrapper = styled.div`
  display: flex;
  gap: 1.5rem;
  justify-content: center;
`;

const show = keyframes`
  from {
    opacity: 0;
  }
  
  to {
    opacity: 1;
  }
`;

export const LevelTooltip = styled(Tooltip)<{ visible: boolean }>`
  display: ${({ visible }) => !visible && 'none'};
  position: absolute;
  top: 10%;
  left: 70%;

  animation: ${show} 0.2s ease;
`;

export const SOSTooltip = styled(Tooltip)<{ visible: boolean }>`
  display: ${({ visible }) => !visible && 'none'};
  position: absolute;
  top: 15%;
  right: 105%;

  animation: ${show} 0.2s ease;
`;

const InputCaption = styled.span`
  font-size: ${FONT_SIZE.SMALL};
`;

export const Toybox = styled(ToyboxIcon)`
  position: absolute;
  height: 2rem;
  top: -6px;
  right: 0;
`;

export default {
  FormContainer,
  MarkdownContainer,
  MarkdownWrapper,
  VerticalWrapper,
  InputsContainer,
  StretchWrapper,
  LevelWrapper,
  SOSLabel,
  ButtonsWrapper,
  InputCaption,
};
