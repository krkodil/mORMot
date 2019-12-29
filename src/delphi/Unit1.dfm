object Form1: TForm1
  Left = 227
  Top = 201
  BorderStyle = bsSingle
  ClientHeight = 439
  ClientWidth = 940
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -13
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  OnDestroy = FormDestroy
  PixelsPerInch = 96
  TextHeight = 16
  object StatusBar1: TStatusBar
    Left = 0
    Top = 420
    Width = 940
    Height = 19
    Panels = <>
    SimplePanel = True
  end
  object txtUser: TEdit
    Left = 24
    Top = 16
    Width = 121
    Height = 24
    TabOrder = 1
    Text = 'User'
  end
  object txtPassword: TEdit
    Left = 152
    Top = 16
    Width = 121
    Height = 24
    TabOrder = 2
    Text = 'synopse'
  end
  object btnLogin: TButton
    Left = 280
    Top = 15
    Width = 75
    Height = 25
    Caption = 'Login'
    TabOrder = 3
    OnClick = FormCreate
  end
  object Panel1: TPanel
    Left = 0
    Top = 64
    Width = 940
    Height = 356
    Align = alBottom
    BevelOuter = bvNone
    Enabled = False
    TabOrder = 4
    object Label1: TLabel
      Left = 24
      Top = 16
      Width = 67
      Height = 16
      Caption = 'Your name:'
    end
    object Label2: TLabel
      Left = 24
      Top = 72
      Width = 86
      Height = 16
      Caption = 'Your message:'
    end
    object lblA: TLabel
      Left = 328
      Top = 26
      Width = 17
      Height = 16
      Caption = 'A='
    end
    object lblB: TLabel
      Left = 328
      Top = 74
      Width = 16
      Height = 16
      Caption = 'B='
    end
    object lblResult: TLabel
      Left = 348
      Top = 104
      Width = 184
      Height = 16
      Caption = 'Enter numbers, then Call Server'
    end
    object QuestionMemo: TMemo
      Left = 16
      Top = 88
      Width = 257
      Height = 193
      TabOrder = 0
    end
    object NameEdit: TEdit
      Left = 16
      Top = 32
      Width = 81
      Height = 24
      TabOrder = 1
    end
    object AddButton: TButton
      Left = 16
      Top = 296
      Width = 145
      Height = 25
      Caption = 'Add the message'
      TabOrder = 2
      OnClick = AddButtonClick
    end
    object QuitButton: TButton
      Left = 184
      Top = 296
      Width = 75
      Height = 25
      Caption = 'Quit'
      TabOrder = 3
      OnClick = QuitButtonClick
    end
    object FindButton: TButton
      Left = 104
      Top = 32
      Width = 169
      Height = 25
      Caption = 'Find a previous message'
      TabOrder = 4
      OnClick = FindButtonClick
    end
    object edtA: TEdit
      Left = 352
      Top = 24
      Width = 153
      Height = 24
      TabOrder = 5
    end
    object edtB: TEdit
      Left = 352
      Top = 72
      Width = 153
      Height = 24
      TabOrder = 6
    end
    object btnCall: TButton
      Left = 512
      Top = 24
      Width = 97
      Height = 73
      Caption = 'Call Server'
      TabOrder = 7
      OnClick = btnCallClick
    end
    object Button1: TButton
      Left = 616
      Top = 136
      Width = 75
      Height = 25
      Caption = 'GetFileList'
      TabOrder = 8
      OnClick = Button1Click
    end
    object Edit1: TEdit
      Left = 288
      Top = 136
      Width = 321
      Height = 24
      TabOrder = 9
      Text = '.\'
    end
    object ListView1: TListView
      Left = 288
      Top = 168
      Width = 617
      Height = 150
      Columns = <
        item
          Caption = 'Name'
          Width = 200
        end
        item
          Caption = 'Size'
          Width = 90
        end
        item
          Caption = 'Modified'
          Width = 140
        end
        item
          Caption = 'Version'
          Width = 150
        end>
      TabOrder = 10
      ViewStyle = vsReport
    end
  end
end
